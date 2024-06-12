/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.foundation.ir.export

import hu.simplexion.adaptive.kotlin.common.AbstractIrBuilder
import hu.simplexion.adaptive.kotlin.common.functionByName
import hu.simplexion.adaptive.kotlin.foundation.FqNames
import hu.simplexion.adaptive.kotlin.foundation.Indices
import hu.simplexion.adaptive.kotlin.foundation.Strings
import hu.simplexion.adaptive.kotlin.foundation.ir.FoundationPluginContext
import hu.simplexion.adaptive.kotlin.foundation.ir.util.AdaptiveAnnotationBasedExtension
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.backend.jvm.ir.createJvmIrBuilder
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionExpressionImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrVarargImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrAnonymousInitializerSymbolImpl
import org.jetbrains.kotlin.ir.types.IrTypeSubstitutor
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.name.Name

class CollectorTransform(
    override val pluginContext: FoundationPluginContext
) : IrElementVisitorVoid, AdaptiveAnnotationBasedExtension, AbstractIrBuilder {

    val collectorClasses = mutableListOf<IrClass>()
    val actualClasses = mutableListOf<AdaptiveActualClass>()

    override fun visitModuleFragment(declaration: IrModuleFragment) {

        super.visitModuleFragment(declaration)

        for (collectorClass in collectorClasses) {
            transformCollector(collectorClass)
        }
    }

    override fun visitClass(declaration: IrClass) {

        if (declaration.hasAnnotation(FqNames.COLLECT)) {
            collectorClasses += declaration
            return
        }

        val annotation = declaration.getAnnotation(FqNames.ADAPTIVE_ACTUAL) ?: return

        actualClasses += AdaptiveActualClass(
            namespace = annotation.getAnnotationStringValue() !!,
            irClass = declaration
        )

    }

    fun transformCollector(declaration: IrClass) {

        irFactory.createAnonymousInitializer(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            origin = IrDeclarationOrigin.DEFINED,
            symbol = IrAnonymousInitializerSymbolImpl(),
            isStatic = false
        ).also { initFun ->
            initFun.parent = declaration
            declaration.declarations += initFun

            initFun.body = DeclarationIrBuilder(irContext, initFun.symbol).irBlockBody {

                for (actualClass in actualClasses) {
                    // TODO not sure about visibility restriction in the collector
                    if (actualClass.irClass.visibility == DescriptorVisibilities.PUBLIC) {
                        callSet(declaration, actualClass.key, actualClass.irClass)
                    }
                }

                for (armClass in pluginContext.armClasses) {
                    if (armClass.originalFunction.visibility == DescriptorVisibilities.PUBLIC) {
                        callSet(declaration, armClass.fqName.asString(), armClass.irClass)
                    }
                }
            }
        }
    }

    fun callSet(collectorClass: IrClass, key: String, irClass: IrClass) {

        val constructor = irClass.constructors.first { it.isPrimary }
        check(constructor.valueParameters.size == 3) { "invalid primary constructor (valueParameters.size != 3): ${constructor.dumpKotlinLike()}" }

        val adapterType = constructor.valueParameters.first().type
        check(adapterType.isSubtypeOfClass(pluginContext.adaptiveAdapterClass)) { "first constructor parameter is not an adapter: ${constructor.dumpKotlinLike()}" }

        IrCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.unitType,
            collectorClass.functionByName { Strings.SET },
            0, 2,
        ).also { call ->

            val lambda = irFactory.buildFun {
                name = Name.special("<anonymous>")
                returnType = irClass.defaultType
                visibility = DescriptorVisibilities.LOCAL
                modality = Modality.FINAL
                origin = IrDeclarationOrigin.LOCAL_FUNCTION_FOR_LAMBDA
                isSuspend = false
            }.also { lambda ->
                lambda.dispatchReceiverParameter = null
                lambda.extensionReceiverParameter = null
                lambda.parent = collectorClass

                val parent = lambda.addValueParameter(Strings.PARENT, pluginContext.adaptiveFragmentType)
                val index = lambda.addValueParameter(Strings.DECLARATION_INDEX, irBuiltIns.intType)

                val adapter = irImplicitAs(adapterType, irGetValue(pluginContext.adapter, irGet(parent)))

                lambda.body = DeclarationIrBuilder(irContext, lambda.symbol).irBlockBody {
                    IrConstructorCallImpl(
                        SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                        irClass.defaultType,
                        irClass.constructors.first().symbol,
                        typeArgumentsCount = 0,
                        constructorTypeArgumentsCount = 0,
                        Indices.ADAPTIVE_GENERATED_FRAGMENT_ARGUMENT_COUNT
                    ).also { call ->
                        call.putValueArgument(Indices.ADAPTIVE_FRAGMENT_ADAPTER, adapter)
                        call.putValueArgument(Indices.ADAPTIVE_FRAGMENT_PARENT, irGet(parent))
                        call.putValueArgument(Indices.ADAPTIVE_FRAGMENT_INDEX, irGet(index))
                    }
                }
            }

            val funExpr = IrFunctionExpressionImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                irClass.defaultType,
                lambda,
                IrStatementOrigin.ANONYMOUS_FUNCTION
            )

            call.dispatchReceiver = irGet(collectorClass.thisReceiver())

            call.putValueArgument(0, irConst(key))
            call.putValueArgument(1, funExpr)
        }
    }

}
