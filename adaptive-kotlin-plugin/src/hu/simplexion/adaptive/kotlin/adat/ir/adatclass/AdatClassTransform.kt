/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir.adatclass

import hu.simplexion.adaptive.kotlin.adat.AdatPluginKey
import hu.simplexion.adaptive.kotlin.adat.Names
import hu.simplexion.adaptive.kotlin.adat.ir.AdatIrBuilder
import hu.simplexion.adaptive.kotlin.adat.ir.AdatPluginContext
import hu.simplexion.adaptive.kotlin.adat.ir.adatcompanion.AdatCompanionTransform
import hu.simplexion.adaptive.kotlin.adat.ir.metadata.MetadataVisitor
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.util.isFakeOverride
import org.jetbrains.kotlin.ir.util.isSubclassOf
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptVoid

class AdatClassTransform(
    override val pluginContext: AdatPluginContext,
    private val adatClass : IrClass
) : IrElementVisitorVoid, AdatIrBuilder {

    val properties = MetadataVisitor(pluginContext, adatClass).also { adatClass.acceptVoid(it) }.properties

    override fun visitClass(declaration: IrClass) {
        when {
            declaration.isSubclassOf(pluginContext.adatCompanion.owner) -> {
                AdatCompanionTransform(pluginContext, declaration, properties.map { it.metadata }).transform()
            }
        }
    }

    override fun visitProperty(declaration: IrProperty) {
        when (declaration.name) {
            Names.ADAT_COMPANION -> adatCompanion(adatClass, declaration)
        }
    }

    override fun visitFunction(declaration: IrFunction) {
        if (declaration.isFakeOverride) return

        when (declaration.name) {
            Names.GEN_GET_VALUE -> genGetValue(declaration, properties)
            Names.GEN_SET_VALUE -> genSetValue(declaration, properties)
            Names.EQUALS -> equals(adatClass, declaration)
            Names.HASHCODE -> hashCode(adatClass, declaration)
            Names.TO_STRING -> toString(adatClass, declaration)
        }
    }

    override fun visitConstructor(declaration: IrConstructor) {
        if (declaration.isFakeOverride) return
        if (declaration.origin != AdatPluginKey.origin) return

        when (declaration.valueParameters.size) {
            0 -> emptyConstructor(adatClass, declaration, properties)
            1 -> arrayConstructor(adatClass, declaration, properties)
        }
    }

}
