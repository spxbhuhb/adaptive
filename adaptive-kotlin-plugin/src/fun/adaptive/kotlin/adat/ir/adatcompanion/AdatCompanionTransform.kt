/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.adat.ir.adatcompanion

import `fun`.adaptive.adat.metadata.AdatDescriptorMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.kotlin.adat.AdatPluginKey
import `fun`.adaptive.kotlin.adat.Names
import `fun`.adaptive.kotlin.adat.ir.AdatIrBuilder
import `fun`.adaptive.kotlin.adat.ir.AdatPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
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
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid

class AdatCompanionTransform(
    override val pluginContext: AdatPluginContext,
    val companionClass: IrClass,
    val properties: List<AdatPropertyMetadata>
) : IrElementVisitorVoid, AdatIrBuilder {

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

        companionClass.acceptChildrenVoid(this)

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

    override fun visitProperty(declaration: IrProperty) {
        when (declaration.name) {
            Names.ADAT_METADATA -> adatMetadata(companionClass, declaration, properties)
            Names.ADAT_WIREFORMAT -> adatWireFormat(companionClass, declaration)
            Names.ADAT_DESCRIPTORS -> adatDescriptors(companionClass, declaration)
            Names.WIREFORMAT_NAME -> wireFormatName(companionClass, declaration)

        }
    }

    override fun visitFunction(declaration: IrFunction) {
        if (declaration.isFakeOverride) return
        if (declaration.origin != AdatPluginKey.origin) return

        when (declaration.name) {
            Names.NEW_INSTANCE -> {
                if (declaration.valueParameters.isEmpty()) {
                    newInstanceEmpty(companionClass, declaration)
                } else {
                    newInstanceArray(companionClass, declaration)
                }
            }
        }
    }

}
