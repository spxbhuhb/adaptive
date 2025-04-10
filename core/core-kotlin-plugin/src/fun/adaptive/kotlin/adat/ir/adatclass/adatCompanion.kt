/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.adat.ir.adatclass

import `fun`.adaptive.kotlin.adat.ir.AdatIrBuilder
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.util.companionObject

fun AdatIrBuilder.adatCompanion(
    adatClass: IrClass,
    adatCompanion: IrProperty
) {
    val getter = adatCompanion.getter !!

    getter.origin = IrDeclarationOrigin.DEFINED

    getter.body = DeclarationIrBuilder(pluginContext.irContext, getter.symbol).irBlockBody {
        + irReturn(
            irGetObject(adatClass.companionObject() !!.symbol)
        )
    }
}
