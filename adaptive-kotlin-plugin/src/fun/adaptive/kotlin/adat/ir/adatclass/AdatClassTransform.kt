/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.adat.ir.adatclass

import `fun`.adaptive.kotlin.adat.AdatPluginKey
import `fun`.adaptive.kotlin.adat.Names
import `fun`.adaptive.kotlin.adat.ir.AdatIrBuilder
import `fun`.adaptive.kotlin.adat.ir.AdatPluginContext
import `fun`.adaptive.kotlin.adat.ir.metadata.MetadataVisitor
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.util.isFakeOverride
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptVoid

class AdatClassTransform(
    override val pluginContext: AdatPluginContext,
    private val adatClass : IrClass
) : IrElementVisitorVoid, AdatIrBuilder {

    val metadata = MetadataVisitor(pluginContext, adatClass).also { adatClass.acceptVoid(it) }

    override fun visitProperty(declaration: IrProperty) {
        if (declaration.origin != AdatPluginKey.origin) return

        when (declaration.name) {
            Names.ADAT_COMPANION -> adatCompanion(adatClass, declaration)
            Names.ADAT_CONTEXT -> adatContext(declaration)
        }
    }

    override fun visitFunction(declaration: IrFunction) {
        if (declaration.isFakeOverride) return
        if (declaration.origin != AdatPluginKey.origin) return

        when (declaration.name) {
            Names.GEN_GET_VALUE -> genGetValue(declaration, metadata.properties)
            Names.GEN_SET_VALUE -> genSetValue(declaration, metadata.properties)
            Names.EQUALS -> equals(adatClass, declaration)
            Names.HASHCODE -> hashCode(adatClass, declaration)
            Names.TO_STRING -> toString(adatClass, declaration)
        }
    }

    override fun visitConstructor(declaration: IrConstructor) {
        if (declaration.isFakeOverride) return
        if (declaration.origin != AdatPluginKey.origin) return

        when (declaration.valueParameters.size) {
            0 -> emptyConstructor(adatClass, declaration, metadata.properties)
            1 -> arrayConstructor(adatClass, declaration, metadata.properties)
        }
    }

}
