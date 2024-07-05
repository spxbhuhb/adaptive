/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat.wireformat

import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.metadata.AdatPropertyMetaData
import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder

class AdatPropertyWireFormat<T>(
    val property : AdatPropertyMetaData,
    val wireFormat : WireFormat<T>
) {

    val nullable : Boolean = property.signature.endsWith("?")

    fun encode(encoder: WireFormatEncoder, instance: AdatClass<*>) {
        @Suppress("UNCHECKED_CAST")
        wireFormat.wireFormatEncode(encoder, property.index, property.name, instance.getValue(property.index) as T?)
    }

    fun encode(encoder : WireFormatEncoder, values : Array<Any?>) {
        @Suppress("UNCHECKED_CAST")
        wireFormat.wireFormatEncode(encoder, property.index, property.name, values[property.index] as T?)
    }

    fun decode(decoder: WireFormatDecoder<*>, instance: AdatClass<*>) {
        val value = wireFormat.wireFormatDecode(decoder, property.index, property.name)
        instance.setValue(property.index, if (nullable) value else checkNotNull(value))
    }

    fun decode(decoder: WireFormatDecoder<*>, values:Array<Any?>) {
        val value = wireFormat.wireFormatDecode(decoder, property.index, property.name)
        values[property.index] = if (nullable) value else checkNotNull(value)
    }
}