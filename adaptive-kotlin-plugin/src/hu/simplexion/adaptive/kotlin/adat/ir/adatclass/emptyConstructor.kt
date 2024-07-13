/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir.adatclass

import hu.simplexion.adaptive.kotlin.adat.ir.AdatIrBuilder
import hu.simplexion.adaptive.kotlin.adat.ir.metadata.PropertyData
import hu.simplexion.adaptive.kotlin.adat.ir.sensible.sensibleDefault
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irDelegatingConstructorCall
import org.jetbrains.kotlin.ir.builders.irSetField
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.deepCopyWithSymbols

fun AdatIrBuilder.emptyConstructor(
    adatClass: IrClass,
    constructor: IrConstructor,
    properties: List<PropertyData>
) {

    val primary = adatClass.constructors.first { it.isPrimary }
    if (constructor === primary) return // this should never happen as we use the plugin key when calling `emptyConstructor`

    val remaining = properties.toMutableSet()

    constructor.body = DeclarationIrBuilder(pluginContext.irContext, constructor.symbol).irBlockBody {

        + irDelegatingConstructorCall(
            irBuiltIns.anyClass.constructors.first().owner
        )

        for (parameter in primary.valueParameters) {

            val propertyData = properties.firstOrNull { it.property.name == parameter.name } ?: continue

            val defaultValue = parameter.defaultValue

            val initializer =
                defaultValue?.deepCopyWithSymbols(primary)?.expression
                    ?: sensibleDefault(propertyData.metadata.signature)

            if (initializer != null) {
                + irSetField(
                    receiver = irGet(adatClass.thisReceiver !!),
                    propertyData.property.backingField !!,
                    initializer
                )
            }

            remaining -= propertyData
        }
    }
}