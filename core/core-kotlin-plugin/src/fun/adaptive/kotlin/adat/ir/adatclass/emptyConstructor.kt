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
import org.jetbrains.kotlin.ir.declarations.IrParameterKind
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.deepCopyWithSymbols
import org.jetbrains.kotlin.ir.util.superClass

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
            adatClass.superClass?.constructors
                ?.first { it.parameters.none { it.kind == IrParameterKind.Regular } }
                ?: irBuiltIns.anyClass.constructors.first().owner
        )

        for (parameter in primary.parameters) {
            if (parameter.kind != IrParameterKind.Regular) continue
            val propertyData = properties.firstOrNull { it.property.name == parameter.name } ?: continue

            val defaultValue = parameter.defaultValue

            val initializer = defaultValue?.deepCopyWithSymbols(primary)?.expression

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