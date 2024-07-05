/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir.adatcompanion

import hu.simplexion.adaptive.adat.metadata.AdatClassMetadata
import hu.simplexion.adaptive.adat.metadata.AdatPropertyMetadata
import hu.simplexion.adaptive.kotlin.adat.Strings
import hu.simplexion.adaptive.kotlin.adat.ir.AdatPluginContext
import hu.simplexion.adaptive.kotlin.common.AbstractIrBuilder
import hu.simplexion.adaptive.wireformat.toJson
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.getSimpleFunction
import org.jetbrains.kotlin.ir.util.parentAsClass

class AdatMetadataPropertyTransform(
    override val pluginContext: AdatPluginContext,
    val companionClass: IrClass,
    val properties: List<AdatPropertyMetadata>
) : IrElementTransformerVoidWithContext(), AbstractIrBuilder {

    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
        val adatClass = companionClass.parentAsClass

        val metadata = AdatClassMetadata<Any>(
            version = 1,
            name = adatClass.classId !!.asFqNameString(),
            properties = properties
        )

        declaration.backingField!!.initializer = irFactory.createExpressionBody(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irCall(
                companionClass.getSimpleFunction(Strings.DECODE_METADATA)!!,
                irGet(companionClass.thisReceiver!!),
                irConst(metadata.toJson(AdatClassMetadata).decodeToString())
            )
        )

        return declaration
    }

}
