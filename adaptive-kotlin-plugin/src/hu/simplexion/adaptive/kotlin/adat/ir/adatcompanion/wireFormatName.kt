/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir.adatcompanion

import hu.simplexion.adaptive.kotlin.adat.ir.AdatIrBuilder
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.ir.util.parentAsClass

fun AdatIrBuilder.wireFormatName(
    companionClass: IrClass,
    wireFormatNameProperty: IrProperty
) {
    // TODO maybe generating a getter or using a function directly would be better
    wireFormatNameProperty.backingField !!.initializer = irFactory.createExpressionBody(
        SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
        irConst(companionClass.parentAsClass.kotlinFqName.asString())
    )
}