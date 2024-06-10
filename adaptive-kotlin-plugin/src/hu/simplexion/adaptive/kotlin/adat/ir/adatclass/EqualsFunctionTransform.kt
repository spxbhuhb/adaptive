/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir.adatclass

import hu.simplexion.adaptive.kotlin.adat.AdatPluginKey
import hu.simplexion.adaptive.kotlin.adat.Names
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

class EqualsFunctionTransform(
    override val pluginContext: AdatPluginContext,
    val adatClass: IrClass,
    val equalsFunction: IrFunction
) : IrElementTransformerVoidWithContext(), AbstractIrBuilder {

    override fun visitFunctionNew(declaration: IrFunction): IrStatement {
        equalsFunction.body = DeclarationIrBuilder(irContext, equalsFunction.symbol).irBlockBody {
            + irReturn(
                irCall(
                    adatClass.getSimpleFunction(Names.ADAT_EQUALS.identifier) !!,
                    irGet(equalsFunction.dispatchReceiverParameter !!),
                    irGet(equalsFunction.valueParameters.first())
                )
            )
        }

        return declaration
    }
}
