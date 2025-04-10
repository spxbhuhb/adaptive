/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat.wireformat

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.wireformat.WireFormat
import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.WireFormatEncoder
import `fun`.adaptive.wireformat.toJson

/**
 * WireFormat generated from [AdatClassMetadata].
 *
 * [propertyWireFormats] is lazy to support cross-references between Adat classes. Adat
 * companions create their WireFormat when the companion is initialized and register
 * themselves in the `WireFormatRegistry` after. When they contain another Adat
 * class it is possible that the companion of that class is not loaded yet, resulting
 * the [toPropertyWireFormat] call to fail with a missing WireFormat.
 */
class AdatClassWireFormat<A>(
    val companion: AdatCompanion<A>,
    val metadata: AdatClassMetadata
) : WireFormat<A> {

    override val wireFormatName
        get() = companion.wireFormatName

    val propertyWireFormats by lazy {
        metadata.properties.map {
            try {
                it.toPropertyWireFormat()
            } catch (ex: Exception) {
                throw RuntimeException("wireformat build error for ${metadata.name}.${it.name}\n${metadata.toJson(AdatClassMetadata).decodeToString()}\n${ex.stackTraceToString()}", ex)
            }
        }
    }

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: A): WireFormatEncoder {
        for (propertyWireFormat in propertyWireFormats) {
            propertyWireFormat.encode(encoder, value as AdatClass)
        }
        return encoder
    }

    fun wireFormatEncode(encoder: WireFormatEncoder, values: Array<Any?>): WireFormatEncoder {
        for (propertyWireFormat in propertyWireFormats) {
            propertyWireFormat.encode(encoder, values)
        }
        return encoder
    }

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): A =
        wireFormatDecode(decoder)

    // TODO think about newInstance in manually coded wire formats in `core`
    override fun newInstance(values: Array<Any?>): A =
        companion.newInstance(values)

    fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>?): A {
        val value = arrayOfNulls<Any?>(propertyWireFormats.size)

        for (propertyWireFormat in propertyWireFormats) {
            propertyWireFormat.decode(decoder as WireFormatDecoder<*>, value)
        }

        return companion.newInstance(value)
    }

}