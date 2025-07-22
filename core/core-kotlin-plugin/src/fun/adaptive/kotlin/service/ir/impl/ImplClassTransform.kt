/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.service.ir.impl

import `fun`.adaptive.kotlin.common.*
import `fun`.adaptive.kotlin.service.FqNames
import `fun`.adaptive.kotlin.service.Names
import `fun`.adaptive.kotlin.service.Strings
import `fun`.adaptive.kotlin.service.ir.ServicesPluginContext
import `fun`.adaptive.kotlin.service.ir.util.IrClassBaseBuilder
import `fun`.adaptive.kotlin.wireformat.Signature
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.builders.declarations.buildReceiverParameter
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.IrBranch
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrDelegatingConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrElseBranchImpl
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.types.classOrNull
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

        return declaration
    }

    override fun visitPropertyNew(declaration: IrProperty): IrStatement =
        when (declaration.name) {
            Names.SERVICE_NAME -> transformServiceName(declaration)
            Names.SERVICE_CONTEXT_PROPERTY -> transformServiceContext(declaration)
            else -> declaration
        }

    override fun visitFunctionNew(declaration: IrFunction): IrStatement {

        if (declaration !is IrSimpleFunction) return declaration
        if (! declaration.isSuspend) return declaration
        if (declaration.isFakeOverride) return declaration
        if (declaration.overriddenSymbols.none { it.isServiceFunction() }) return declaration

        implementedServiceFunctions += ServiceFunctionEntry(
            Signature.functionSignature(declaration, pluginContext.adatClass),
            declaration
        )

        return declaration
    }

    private fun IrSimpleFunctionSymbol.isServiceFunction(): Boolean {
        val parentClass = owner.parentClassOrNull ?: return false
        return parentClass.hasAnnotation(FqNames.SERVICE_API)
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
        require(oldPrimary.regularParameterCount == 0) { "Service implementation constructor must not have any parameters. ${transformedClass.kotlinFqName}" }

        oldPrimary.isPrimary = false

        oldPrimary.body = irFactory.createBlockBody(oldPrimary.startOffset, oldPrimary.endOffset).apply {
            statements += IrDelegatingConstructorCallImpl(
                SYNTHETIC_OFFSET,
                SYNTHETIC_OFFSET,
                transformedClass.defaultType,
                newPrimary.symbol,
                typeArgumentsCount = 0
            ).also {
                it.arguments[0] = irNull()
            }
        }
    }

    fun generateDispatch() {
        val dispatch = checkNotNull(transformedClass.getSimpleFunction(Strings.DISPATCH)).owner

        dispatch.body = DeclarationIrBuilder(irContext, dispatch.symbol).irBlockBody {
            + irReturn(
                irBlock(
                    origin = IrStatementOrigin.WHEN
                ) {
                    val funName = irTemporary(irGet(dispatch.firstRegularParameter))
                    + irCall(
                        pluginContext.wireFormatCache.pack,
                        irWhen(
                            pluginContext.wireFormatCache.wireFormatEncoder.defaultType,
                            implementedServiceFunctions.map { dispatchBranch(dispatch, it, funName) }
                                + irInvalidIndexBranch(dispatch, irGet(dispatch.firstRegularParameter))
                        )
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
            pluginContext.wireFormatCache.encodeReturnValue(
                targetType = serviceFunction.function.returnType,
                encoder = irCall(
                    transformedClass.functionByName { Strings.WIREFORMAT_ENCODER },
                    dispatchReceiver = irGet(dispatch.dispatchReceiverParameter !!)
                ),
                value = callServiceFunction(dispatch, serviceFunction.function)
            )
        )

    fun IrBlockBodyBuilder.callServiceFunction(dispatch: IrSimpleFunction, serviceFunction: IrSimpleFunction): IrExpression =
        irCall(
            serviceFunction.symbol
        ).also {
            it.arguments[0] = irGet(dispatch.dispatchReceiverParameter !!)
            var fieldNumber = 1
            serviceFunction.parameters.forEach { parameter ->
                if (parameter.kind != IrParameterKind.Regular) return@forEach
                it.arguments[parameter] = pluginContext.wireFormatCache.decode(fieldNumber++, parameter) { irGet(dispatch.secondRegularParameter) }
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
                typeArgumentsCount = 0
            ).also {
                it.arguments[0] = irGet(fromFun.dispatchReceiverParameter !!)
                it.arguments[1] = getFunName
            }
        )

    fun transformServiceName(declaration: IrProperty): IrStatement {
        val serviceNames = transformedClass.superTypes.mapNotNull { superType ->
            if (superType.classOrNull?.owner?.hasAnnotation(FqNames.SERVICE_API) == true) {
                superType.classFqName !!.asString()
            } else {
                null
            }
        }

        require(serviceNames.isNotEmpty()) { "${transformedClass.kotlinFqName} missing service interface (is '@ServiceApi' missing?)" }
        require(serviceNames.size == 1) {
            "${transformedClass.kotlinFqName} you have to set `serviceName` manually when more than one service is implemented (${serviceNames.joinToString()})"
        }

        transformProperty(
            pluginContext,
            declaration,
            backingField = false
        ) { irConst(serviceNames.first()) }

        return declaration
    }

    fun transformServiceContext(declaration: IrProperty): IrStatement {
        transformProperty(
            pluginContext,
            declaration,
            backingField = true
        ) { irGet(constructor.firstRegularParameter) }

        return declaration
    }
}