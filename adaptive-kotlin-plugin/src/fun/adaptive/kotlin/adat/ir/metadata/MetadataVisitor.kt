/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin.adat.ir.metadata

import `fun`.adaptive.adat.metadata.AdatDescriptorMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.kotlin.adat.Names
import `fun`.adaptive.kotlin.adat.ir.AdatIrBuilder
import `fun`.adaptive.kotlin.adat.ir.AdatPluginContext
import `fun`.adaptive.kotlin.adat.ir.immutable.isImmutable
import `fun`.adaptive.kotlin.wireformat.Signature
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.types.isNullable
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import kotlin.collections.map

class MetadataVisitor(
    override val pluginContext: AdatPluginContext,
    val adatClass: IrClass
) : IrElementVisitorVoid, AdatIrBuilder {

    val properties = mutableListOf<PropertyData>()
    val descriptors = mutableListOf<Pair<String, List<AdatDescriptorMetadata>>>()

    val constructor = adatClass.primaryConstructor !!

    var propertyIndex = 0

    fun zip(): List<AdatPropertyMetadata> =
        properties.map {
            AdatPropertyMetadata(
                it.metadata.name,
                it.metadata.index,
                it.metadata.flags,
                it.metadata.signature,
                descriptors.firstOrNull { d -> d.first == it.property.name.identifier }?.second ?: emptyList()
            )
        }

    override fun visitElement(element: IrElement) {
        element.acceptChildrenVoid(this)
    }

    override fun visitClass(declaration: IrClass) {
        if (declaration != adatClass) return
        super.visitClass(declaration)
    }

    override fun visitProperty(declaration: IrProperty) {
        if (declaration.name == Names.ADAT_COMPANION) return
        if (declaration.name == Names.ADAT_CONTEXT) return
        if (declaration.backingField == null) return

        val signature = Signature.typeSignature(declaration.getter !!.returnType)

        properties += PropertyData(
            declaration,
            AdatPropertyMetadata(
                declaration.name.identifier,
                propertyIndex ++,
                calculateFlags(declaration, signature),
                signature
            )
        )
    }

    fun calculateFlags(declaration: IrProperty, signature: String): Int {
        var flags = 0

        val isVal = ! declaration.isVar
        val backingField = declaration.backingField

        if (isVal) flags = flags or AdatPropertyMetadata.VAL
        if (backingField == null || isImmutable(signature)) flags = flags or AdatPropertyMetadata.IMMUTABLE_VALUE
        if (backingField?.type?.isSubtypeOfClass(pluginContext.adatClass) == true) flags = flags or AdatPropertyMetadata.ADAT_CLASS

        if (backingField?.type?.isNullable() == true) {
            flags = flags or AdatPropertyMetadata.NULLABLE
        }

        val defaultValue = constructor.valueParameters.firstOrNull { it.name == declaration.name }?.defaultValue

        if (defaultValue != null) {
            flags = flags or AdatPropertyMetadata.HAS_DEFAULT
        }

        return flags
    }

    override fun visitFunction(declaration: IrFunction) {
        if (declaration.name != Names.DESCRIPTOR) return
        if (declaration.isFakeOverride) return

        descriptor(adatClass, declaration, descriptors)
    }

}