/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir.adatcompanion

import hu.simplexion.adaptive.kotlin.adat.Names
import hu.simplexion.adaptive.kotlin.adat.ir.AdatPluginContext
import hu.simplexion.adaptive.kotlin.common.AbstractIrBuilder
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.symbols.impl.IrAnonymousInitializerSymbolImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.isFakeOverride
import org.jetbrains.kotlin.ir.util.parentAsClass

class AdatCompanionTransform(
    override val pluginContext: AdatPluginContext,
    val companionClass: IrClass
) : IrElementTransformerVoidWithContext(), AbstractIrBuilder {

    init {
        irFactory.createAnonymousInitializer(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            origin = IrDeclarationOrigin.DEFINED,
            symbol = IrAnonymousInitializerSymbolImpl(),
            isStatic = false
        ).also { initFun ->
            initFun.parent = companionClass
            companionClass.declarations += initFun

            initFun.body = DeclarationIrBuilder(irContext, initFun.symbol).irBlockBody {
                + irCall(
                    pluginContext.wireFormatRegistrySet,
                    irGetObject(pluginContext.wireFormatRegistry),
                    irConst(companionClass.parentAsClass.classId !!.asFqNameString()),
                    irGet(companionClass.thisReceiver !!)
                )
            }
        }
    }

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
