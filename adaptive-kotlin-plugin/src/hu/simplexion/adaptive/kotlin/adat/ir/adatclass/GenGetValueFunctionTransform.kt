/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir.adatclass

import hu.simplexion.adaptive.kotlin.adat.ir.AdatPluginContext
import hu.simplexion.adaptive.kotlin.adat.ir.metadata.PropertyData
import hu.simplexion.adaptive.kotlin.common.AbstractIrBuilder
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.builders.irWhen
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.impl.IrBranchImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrElseBranchImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET

class GenGetValueFunctionTransform(
    override val pluginContext: AdatPluginContext,
    val adatClass: IrClass,
    val properties: List<PropertyData>,
    val function: IrFunction
) : IrElementTransformerVoidWithContext(), AbstractIrBuilder {

    fun transform() {
        function.transform(this, null)
    }

    override fun visitFunctionNew(declaration: IrFunction): IrStatement {
        function as IrSimpleFunction

        function.body = DeclarationIrBuilder(irContext, function.symbol).irBlockBody {
            + irReturn(
                irWhen(
                    irBuiltIns.anyNType,
                    properties.map { transformProperty(it) } +
                        IrElseBranchImpl(
                            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                            irConst(true),
                            irNull() // FIXME throw exception on invalid index
                        )
                )
            )
        }

        return declaration
    }

    fun transformProperty(property: PropertyData) =
        IrBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irEqual(irGet(function.valueParameters.first()), irConst(property.metadata.index)),
            irGetValue(property.property, irGet(function.dispatchReceiverParameter !!))
        )
}
