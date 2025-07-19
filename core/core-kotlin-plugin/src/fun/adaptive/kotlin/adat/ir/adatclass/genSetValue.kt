/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.adat.ir.adatclass

import `fun`.adaptive.kotlin.adat.ir.AdatIrBuilder
import `fun`.adaptive.kotlin.adat.ir.metadata.PropertyData
import `fun`.adaptive.kotlin.common.firstRegularParameter
import `fun`.adaptive.kotlin.common.secondRegularParameter
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irWhen
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.impl.IrBranchImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET

fun AdatIrBuilder.genSetValue(
    genSetValueFunction: IrFunction,
    properties: List<PropertyData>
) {
    genSetValueFunction as IrSimpleFunction

    genSetValueFunction.body = DeclarationIrBuilder(irContext, genSetValueFunction.symbol).irBlockBody {
        val branches = properties.mapNotNull { transformProperty(genSetValueFunction, it) }

        if (branches.isNotEmpty()) { // FIXME throw exception on else
            + irWhen(
                irBuiltIns.unitType,
                branches
            )
        }
    }
}

private fun AdatIrBuilder.transformProperty(genSetValueFunction: IrSimpleFunction, property: PropertyData) =
    if (property.property.isVar) {
        IrBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irEqual(irGet(genSetValueFunction.firstRegularParameter), irConst(property.metadata.index)),
            irSetValue(
                property.property,
                irImplicitAs(
                    property.property.getter !!.returnType,
                    irGet(genSetValueFunction.secondRegularParameter)
                ),
                irGet(genSetValueFunction.dispatchReceiverParameter !!)
            )
        )
    } else {
        null
    }
