/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.adat.ir.adatclass

import `fun`.adaptive.kotlin.adat.ir.AdatIrBuilder
import `fun`.adaptive.kotlin.adat.ir.metadata.PropertyData
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irDelegatingConstructorCall
import org.jetbrains.kotlin.ir.builders.irSetField
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.superClass

fun AdatIrBuilder.arrayConstructor(
    adatClass: IrClass,
    constructor: IrConstructor,
    properties: List<PropertyData>
) {

    val primary = adatClass.constructors.first { it.isPrimary }
    if (constructor === primary) return

    constructor.body = DeclarationIrBuilder(pluginContext.irContext, constructor.symbol).irBlockBody {

        + irDelegatingConstructorCall(
            adatClass.superClass?.constructors?.first { it.valueParameters.isEmpty() } ?: irBuiltIns.anyClass.constructors.first().owner
        )

        val values = constructor.valueParameters.first()

        for (property in properties) {
            val backingField = property.property.backingField !! // TODO handle properties without backing fields?

            + irSetField(
                receiver = irGet(adatClass.thisReceiver !!),
                backingField,
                irImplicitAs(
                    backingField.type,
                    irCall(
                        pluginContext.arrayGet,
                        dispatchReceiver = irGet(values),
                        args = arrayOf(irConst(property.metadata.index))
                    )
                )
            )
        }
    }
}