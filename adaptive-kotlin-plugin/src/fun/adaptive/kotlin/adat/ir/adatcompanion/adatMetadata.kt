/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.adat.ir.adatcompanion

import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.kotlin.adat.Strings
import `fun`.adaptive.kotlin.common.AbstractIrBuilder
import `fun`.adaptive.wireformat.toJson
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.getSimpleFunction
import org.jetbrains.kotlin.ir.util.parentAsClass

fun AbstractIrBuilder.adatMetadata(
    companionClass: IrClass,
    metadataProperty: IrProperty,
    properties: List<AdatPropertyMetadata>
) {

    val adatClass = companionClass.parentAsClass
    var flags = 0

    if (properties.none { it.isMutable }) {
        flags = flags or AdatClassMetadata.IMMUTABLE
    }

    val metadata = AdatClassMetadata<Any>(
        version = 1,
        name = adatClass.classId !!.asFqNameString(),
        flags = flags,
        properties = properties
    )

    metadataProperty.backingField !!.initializer = irFactory.createExpressionBody(
        SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
        irCall(
            companionClass.getSimpleFunction(Strings.DECODE_METADATA) !!,
            irGet(companionClass.thisReceiver !!),
            irConst(metadata.toJson(AdatClassMetadata).decodeToString())
        )
    )

}
