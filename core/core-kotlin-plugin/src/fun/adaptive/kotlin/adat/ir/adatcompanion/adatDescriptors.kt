/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.adat.ir.adatcompanion

import `fun`.adaptive.kotlin.adat.Strings
import `fun`.adaptive.kotlin.common.AbstractIrBuilder
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.getSimpleFunction

fun AbstractIrBuilder.adatDescriptors(
    companionClass: IrClass,
    descriptorProperty: IrProperty
) {

    descriptorProperty.backingField !!.initializer = irFactory.createExpressionBody(
        SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
        irCall(
            companionClass.getSimpleFunction(Strings.GENERATE_DESCRIPTORS) !!,
            irGet(companionClass.thisReceiver !!)
        )
    )

}
