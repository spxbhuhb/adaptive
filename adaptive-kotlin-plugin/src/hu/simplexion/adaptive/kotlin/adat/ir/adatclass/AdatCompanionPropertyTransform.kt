/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir.adatclass

import hu.simplexion.adaptive.kotlin.adat.ir.AdatPluginContext
import hu.simplexion.adaptive.kotlin.common.AbstractIrBuilder
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.util.companionObject

class AdatCompanionPropertyTransform(
    override val pluginContext: AdatPluginContext,
    val adatClass: IrClass
) : IrElementTransformerVoidWithContext(), AbstractIrBuilder {

    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
        val getter = declaration.getter !!

        getter.origin = IrDeclarationOrigin.DEFINED

        getter.body = DeclarationIrBuilder(pluginContext.irContext, getter.symbol).irBlockBody {
            + irReturn(
                irGetObject(adatClass.companionObject() !!.symbol)
            )
        }

        return declaration
    }

}
