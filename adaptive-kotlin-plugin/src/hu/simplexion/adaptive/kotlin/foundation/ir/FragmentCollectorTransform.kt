/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.foundation.ir

import hu.simplexion.adaptive.kotlin.common.AbstractIrBuilder
import hu.simplexion.adaptive.kotlin.common.functionByName
import hu.simplexion.adaptive.kotlin.foundation.FqNames
import hu.simplexion.adaptive.kotlin.foundation.Strings
import hu.simplexion.adaptive.kotlin.foundation.ir.util.AdaptiveAnnotationBasedExtension
import hu.simplexion.adaptive.kotlin.foundation.ir.util.adaptiveClassFqName
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.Visibilities
import org.jetbrains.kotlin.fir.declarations.utils.visibility
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrVarargImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrAnonymousInitializerSymbolImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.companionObject
import org.jetbrains.kotlin.ir.util.hasAnnotation

class FragmentCollectorTransform(
    override val pluginContext: AdaptivePluginContext
) : IrElementTransformerVoidWithContext(), AdaptiveAnnotationBasedExtension, AbstractIrBuilder {

    override fun visitClassNew(declaration: IrClass): IrStatement {
        if (! declaration.hasAnnotation(FqNames.ADAPTIVE_FRAGMENT_COMPANION_COLLECTOR)) {
            return declaration
        }

        irFactory.createAnonymousInitializer(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            origin = IrDeclarationOrigin.DEFINED,
            symbol = IrAnonymousInitializerSymbolImpl(),
            isStatic = false
        ).also { initFun ->
            initFun.parent = declaration
            declaration.declarations += initFun

            initFun.body = DeclarationIrBuilder(irContext, initFun.symbol).irBlockBody {

                + IrCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    irBuiltIns.unitType,
                    declaration.functionByName { Strings.ADD_ALL },
                    0, 1,
                ).apply {

                    dispatchReceiver = irGet(declaration.thisReceiver !!)

                    putValueArgument(
                        0,

                        IrVarargImpl(
                            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                            irBuiltIns.arrayClass.typeWith(pluginContext.adaptiveFragmentCompanionClass.defaultType),
                            pluginContext.adaptiveFragmentCompanionClass.defaultType
                        ).apply {

                            pluginContext.armClasses.forEach {
                                if (it.originalFunction.visibility == DescriptorVisibilities.PUBLIC) {
                                    val companion = checkNotNull(it.irClass.companionObject()) { "missing companion" }
                                    elements += irGetObject(companion.symbol)
                                }
                            }

                        }
                    )
                }
            }

            return declaration
        }
    }
}
