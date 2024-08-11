/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.adat.ir.adatclass

import `fun`.adaptive.kotlin.adat.Names
import `fun`.adaptive.kotlin.adat.ir.AdatIrBuilder
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.util.getSimpleFunction

fun AdatIrBuilder.toString(
    adatClass: IrClass,
    toStringFunction: IrFunction
) {
    toStringFunction.body = DeclarationIrBuilder(irContext, toStringFunction.symbol).irBlockBody {
        + irReturn(
            irCall(
                adatClass.getSimpleFunction(Names.ADAT_TO_STRING.identifier) !!,
                irGet(toStringFunction.dispatchReceiverParameter !!)
            )
        )
    }
}
