/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir.adatcompanion

import hu.simplexion.adaptive.kotlin.adat.Strings
import hu.simplexion.adaptive.kotlin.adat.ir.AdatIrBuilder
import hu.simplexion.adaptive.kotlin.common.propertyGetter
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.parentAsClass

fun AdatIrBuilder.adatWireFormat(
    companionClass: IrClass,
    wireFormatProperty: IrProperty
) {
    wireFormatProperty.backingField !!.initializer = irFactory.createExpressionBody(
        SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
        IrConstructorCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            wireFormatProperty.getter !!.returnType,
            pluginContext.adatClassWireFormat.constructors.first(),
            1, 0, 2
        ).also {
            it.putTypeArgument(0, companionClass.parentAsClass.defaultType)

            it.putValueArgument(0, irGet(companionClass.thisReceiver !!))
            it.putValueArgument(
                1,
                irCall(
                    companionClass.propertyGetter { Strings.ADAT_METADATA },
                    irGet(companionClass.thisReceiver !!)
                )
            )
        }
    )
}