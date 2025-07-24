/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.adat.ir.adatclass

import `fun`.adaptive.kotlin.adat.ir.AdatIrBuilder
import `fun`.adaptive.kotlin.adat.ir.metadata.PropertyData
import `fun`.adaptive.kotlin.common.firstRegularParameter
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.builders.irWhen
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.impl.IrBranchImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrElseBranchImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET

fun AdatIrBuilder.genGetValue(
    genGetValueFunction: IrFunction,
    properties: List<PropertyData>
) {
    genGetValueFunction as IrSimpleFunction

    genGetValueFunction.body = DeclarationIrBuilder(irContext, genGetValueFunction.symbol).irBlockBody {
        + irReturn(
            irWhen(
                irBuiltIns.anyNType,
                properties.map { transformProperty(genGetValueFunction, it) } +
                    IrElseBranchImpl(
                        SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                        irConst(true),
                        irNull() // FIXME throw exception on invalid index
                    )
            )
        )
    }
}

private fun AdatIrBuilder.transformProperty(genGetValueFunction: IrFunction, property: PropertyData) =
    IrBranchImpl(
        SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
        irEqual(irGet(genGetValueFunction.firstRegularParameter), irConst(property.metadata.index)),
        irGetValue(property.property, irGet(genGetValueFunction.dispatchReceiverParameter !!))
    )

