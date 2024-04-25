/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.services.ir.impl

import hu.simplexion.z2.kotlin.common.AbstractIrBuilder
import hu.simplexion.z2.kotlin.common.functionByName
import hu.simplexion.z2.kotlin.common.propertyGetter
import hu.simplexion.z2.kotlin.services.Indices
import hu.simplexion.z2.kotlin.services.Names
import hu.simplexion.z2.kotlin.services.Strings
import hu.simplexion.z2.kotlin.services.ir.ServicesPluginContext
import hu.simplexion.z2.kotlin.services.ir.util.IrClassBaseBuilder
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.ir.addDispatchReceiver
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.IrBranch
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrDelegatingConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrElseBranchImpl
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.makeNullable
import org.jetbrains.kotlin.ir.util.*

class ImplClassTransform(
    override val pluginContext: ServicesPluginContext
) : IrElementTransformerVoidWithContext(), AbstractIrBuilder, IrClassBaseBuilder {

    lateinit var transformedClass: IrClass

    lateinit var constructor: IrConstructor
    lateinit var serviceContextGetter: IrSimpleFunctionSymbol

    val implementedServiceFunctions = mutableListOf<ServiceFunctionEntry>()

    class ServiceFunctionEntry(
        val signature: String,
        val function: IrSimpleFunction
    )

    override fun visitClassNew(declaration: IrClass): IrStatement {
        if (::transformedClass.isInitialized) return declaration

        transformedClass = declaration
        serviceContextGetter = checkNotNull(declaration.getPropertyGetter(Strings.SERVICE_CONTEXT_PROPERTY))

        transformConstructor()
        NewInstance(pluginContext, this).build()

        super.visitClassNew(declaration)

        generateDispatch()

        if (pluginContext.options.dumpKotlinLike) {
            pluginContext.debug("KOTLIN LIKE") { "\n\n" + transformedClass.dumpKotlinLike(KotlinLikeDumpOptions(printFakeOverridesStrategy = FakeOverridesStrategy.NONE)) }
        }

        return declaration
    }

    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
        when (declaration.name) {
            Names.FQ_NAME -> ServiceNamePropertyTransform(pluginContext, transformedClass, declaration).build()
            Names.SERVICE_CONTEXT_PROPERTY -> ServiceContextPropertyTransform(pluginContext, this, declaration).build()
        }

        return declaration
    }

    override fun visitFunctionNew(declaration: IrFunction): IrStatement {

        if (declaration !is IrSimpleFunction) return declaration
        if (! declaration.isSuspend) return declaration
        if (declaration.isFakeOverride) return declaration
        if (declaration.overriddenSymbols.none { it.isServiceFunction() }) return declaration

        implementedServiceFunctions += ServiceFunctionEntry(
            pluginContext.wireFormatCache.signature(declaration),
            declaration
        )

        return declaration
    }

    private fun IrSimpleFunctionSymbol.isServiceFunction(): Boolean {
        val parentClass = owner.parentClassOrNull ?: return false
        return parentClass.isSubclassOf(pluginContext.serviceClass.owner)
    }

    fun transformConstructor() {
        val constructors = transformedClass.constructors.toList()
        require(constructors.size == 1) { "Service implementations must have only one constructor: ${transformedClass.kotlinFqName}" }

        val newPrimary = constructor(transformedClass) // this adds an empty body
        newPrimary.addValueParameter("serviceContext".name, pluginContext.serviceContextType.makeNullable())
        constructor = newPrimary

        // replace the body of the old primary constructor - which must have no parameters -
        // with a body that calls the new primary with a null context

        val oldPrimary = constructors.first()
        require(oldPrimary.valueParameters.isEmpty()) { "Service implementation constructor must not have any parameters. ${transformedClass.kotlinFqName}" }

        oldPrimary.isPrimary = false

        oldPrimary.body = irFactory.createBlockBody(oldPrimary.startOffset, oldPrimary.endOffset).apply {
            statements += IrDelegatingConstructorCallImpl.fromSymbolOwner(
                SYNTHETIC_OFFSET,
                SYNTHETIC_OFFSET,
                transformedClass.defaultType,
                newPrimary.symbol,
                typeArgumentsCount = 0,
                valueArgumentsCount = 1
            ).also {
                it.putValueArgument(0, irNull())
            }
        }
    }

    fun generateDispatch() {
        val dispatch = checkNotNull(transformedClass.getSimpleFunction(Strings.DISPATCH)).owner
        if (! dispatch.isFakeOverride) return

        dispatch.isFakeOverride = false
        dispatch.origin = IrDeclarationOrigin.DEFINED

        dispatch.addDispatchReceiver {// replace the interface in the dispatcher with the class
            type = transformedClass.defaultType
        }

        dispatch.body = DeclarationIrBuilder(irContext, dispatch.symbol).irBlockBody {
            + irReturn(
                irBlock(
                    origin = IrStatementOrigin.WHEN
                ) {
                    val funName = irTemporary(irGet(dispatch.valueParameters[Indices.DISPATCH_FUN_NAME]))
                    + irWhen(
                        irBuiltIns.byteArray.defaultType,
                        implementedServiceFunctions.map { dispatchBranch(dispatch, it, funName) }
                            + irInvalidIndexBranch(dispatch, irGet(dispatch.valueParameters[Indices.DISPATCH_FUN_NAME]))
                    )
                }
            )
        }
    }

    fun IrBlockBodyBuilder.dispatchBranch(dispatch: IrSimpleFunction, serviceFunction: ServiceFunctionEntry, funName: IrVariable): IrBranch =
        irBranch(
            irEquals(
                irGet(funName),
                irConst(serviceFunction.signature),
                IrStatementOrigin.EQEQ
            ),
            pluginContext.wireFormatCache.standaloneEncode(
                targetType = serviceFunction.function.returnType,
                standalone = irCall(
                    transformedClass.propertyGetter { Strings.WIREFORMAT_STANDALONE_PROPERTY },
                    dispatchReceiver = irGet(dispatch.dispatchReceiverParameter !!)
                ),
                value = callServiceFunction(dispatch, serviceFunction.function)
            )
        )

    fun IrBlockBodyBuilder.callServiceFunction(dispatch: IrSimpleFunction, serviceFunction: IrSimpleFunction): IrExpression =
        irCall(
            serviceFunction.symbol,
            dispatchReceiver = irGet(dispatch.dispatchReceiverParameter !!)
        ).also {
            serviceFunction.valueParameters.forEachIndexed { fieldNumber, valueParameter ->
                it.putValueArgument(
                    fieldNumber,
                    pluginContext.wireFormatCache.decode(fieldNumber + 1, valueParameter) { irGet(dispatch.valueParameters[Indices.DISPATCH_PAYLOAD]) }
                )
            }
        }

    private fun irInvalidIndexBranch(fromFun: IrSimpleFunction, getFunName: IrExpression) =
        IrElseBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irConst(true),
            IrCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                irBuiltIns.nothingType,
                transformedClass.functionByName { "unknownFunction" },
                0, 1
            ).also {
                it.dispatchReceiver = irGet(fromFun.dispatchReceiverParameter !!)
                it.putValueArgument(0, getFunName)
            }
        )
}