/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir.adatcompanion

import hu.simplexion.adaptive.kotlin.adat.AdatPluginKey
import hu.simplexion.adaptive.kotlin.adat.ir.AdatPluginContext
import hu.simplexion.adaptive.kotlin.common.AbstractIrBuilder
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.util.*

class NewInstanceFunctionTransform(
    override val pluginContext: AdatPluginContext,
    val companionClass : IrClass,
    val newInstanceFunction: IrFunction
) : IrElementTransformerVoidWithContext(), AbstractIrBuilder {

    override fun visitFunctionNew(declaration: IrFunction): IrStatement {
        newInstanceFunction.body = DeclarationIrBuilder(pluginContext.irContext, newInstanceFunction.symbol).irBlockBody {
            + irReturn(
                IrConstructorCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    declaration.returnType,
                    companionClass.parentAsClass.constructors.first{ it.valueParameters.size == 1 && it.origin == AdatPluginKey.origin }.symbol,
                    0, 0, 1
                ).also {
                    it.putValueArgument(0, irGet(declaration.valueParameters[0]))
                }
            )
        }
        return declaration
    }
}
