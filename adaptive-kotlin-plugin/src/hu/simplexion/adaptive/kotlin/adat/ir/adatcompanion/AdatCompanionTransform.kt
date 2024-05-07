/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir.adatcompanion

import hu.simplexion.adaptive.kotlin.adat.Names
import hu.simplexion.adaptive.kotlin.adat.ir.AdatPluginContext
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.util.isFakeOverride

class AdatCompanionTransform(
    private val pluginContext: AdatPluginContext,
    val companionClass : IrClass
) : IrElementTransformerVoidWithContext() {

    override fun visitPropertyNew(declaration: IrProperty): IrStatement {

        when (declaration.name) {
            Names.ADAT_METADATA -> declaration.transform(AdatMetaDataPropertyTransform(pluginContext, companionClass), null)
            Names.ADAT_WIREFORMAT -> declaration.transform(AdatWireFormatPropertyTransform(pluginContext, companionClass), null)
        }

        return declaration
    }

    override fun visitFunctionNew(declaration: IrFunction): IrStatement {
        if (declaration.isFakeOverride) return declaration

        when (declaration.name) {
            Names.NEW_INSTANCE -> declaration.transform(NewInstanceFunctionTransform(pluginContext, companionClass, declaration), null)
        }

        return declaration
    }

}
