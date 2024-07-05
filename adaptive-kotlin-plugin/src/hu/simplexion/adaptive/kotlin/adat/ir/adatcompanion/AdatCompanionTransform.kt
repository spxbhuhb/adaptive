/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir.adatcompanion

import hu.simplexion.adaptive.adat.metadata.AdatPropertyMetadata
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
    val companionClass: IrClass,
    val properties: List<AdatPropertyMetadata>
) : IrElementTransformerVoidWithContext(), AbstractIrBuilder {

    fun transform() {
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

        companionClass.transform(this, null)

        // reorder properties so metadata is before wireformat
        // FIR2IR does not guarantee the order for generated properties
        // it's important to make it right because the wireformat initialized depends on the metadata one

        val metadataIndex = companionClass.declarations.indexOfFirst { it is IrProperty && it.name == Names.ADAT_METADATA }
        val wireformatIndex = companionClass.declarations.indexOfFirst { it is IrProperty && it.name == Names.ADAT_WIREFORMAT }

        if (metadataIndex > wireformatIndex) {
            val metadata = companionClass.declarations[metadataIndex]
            val wireformat = companionClass.declarations[wireformatIndex]
            companionClass.declarations[wireformatIndex] = metadata
            companionClass.declarations[metadataIndex] = wireformat
        }
    }

    override fun visitPropertyNew(declaration: IrProperty): IrStatement {

        when (declaration.name) {
            Names.ADAT_METADATA -> declaration.transform(AdatMetadataPropertyTransform(pluginContext, companionClass, properties), null)
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
