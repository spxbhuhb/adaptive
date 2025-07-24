/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.adat.ir.adatcompanion

import `fun`.adaptive.kotlin.adat.Strings
import `fun`.adaptive.kotlin.adat.ir.AdatIrBuilder
import `fun`.adaptive.kotlin.common.propertyGetter
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.parentAsClass
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl

fun AdatIrBuilder.adatWireFormat(
    companionClass: IrClass,
    wireFormatProperty: IrProperty
) {
    val adatClassType = companionClass.parentAsClass.defaultType

    wireFormatProperty.backingField !!.initializer = irFactory.createExpressionBody(
        SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
        IrConstructorCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            type = pluginContext.adatClassWireFormat.owner.typeWith(adatClassType),
            symbol = pluginContext.adatClassWireFormat.constructors.first(),
            typeArgumentsCount = 1,
            constructorTypeArgumentsCount = 0
        ).also {
            it.typeArguments[0] = adatClassType

            it.arguments[0] = irGet(companionClass.thisReceiver !!)
            it.arguments[1] =
                irCall(
                    companionClass.propertyGetter { Strings.ADAT_METADATA },
                    irGet(companionClass.thisReceiver !!)
                )
        }
    )
}