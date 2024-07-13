/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir.adatclass

import hu.simplexion.adaptive.kotlin.adat.ir.AdatIrBuilder
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET

fun AdatIrBuilder.adatContext(
    adatContext: IrProperty
) {
    adatContext.backingField !!.initializer = irFactory.createExpressionBody(
        SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
        irNull()
    )
}
