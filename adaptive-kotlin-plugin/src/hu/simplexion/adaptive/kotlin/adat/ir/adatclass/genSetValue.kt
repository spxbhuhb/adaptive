/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir.adatclass

import hu.simplexion.adaptive.kotlin.adat.ir.AdatIrBuilder
import hu.simplexion.adaptive.kotlin.adat.ir.metadata.PropertyData
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
        + irWhen(
            irBuiltIns.unitType,
            properties.mapNotNull { transformProperty(genSetValueFunction, it) } // FIXME throw exception on else
        )
    }
}

private fun AdatIrBuilder.transformProperty(genSetValueFunction: IrSimpleFunction, property: PropertyData) =
    if (property.property.isVar) {
        IrBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irEqual(irGet(genSetValueFunction.valueParameters.first()), irConst(property.metadata.index)),
            irSetValue(
                property.property,
                irImplicitAs(
                    property.property.getter !!.returnType,
                    irGet(genSetValueFunction.valueParameters[1])
                ),
                irGet(genSetValueFunction.dispatchReceiverParameter !!)
            )
        )
    } else {
        null
    }
