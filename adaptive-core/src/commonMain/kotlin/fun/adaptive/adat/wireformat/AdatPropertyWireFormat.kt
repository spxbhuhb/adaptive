/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat.wireformat

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.wireformat.WireFormat
import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.WireFormatEncoder

class AdatPropertyWireFormat<T>(
    val metadata: AdatPropertyMetadata,
    val wireFormat : WireFormat<T>
) {

    val index: Int
        get() = metadata.index

    val name: String
        get() = metadata.name

    fun encode(encoder: WireFormatEncoder, instance: AdatClass) {
        @Suppress("UNCHECKED_CAST")
        wireFormat.wireFormatEncode(encoder, metadata.index, metadata.name, instance.getValue(metadata.index) as T?)
    }

    fun encode(encoder : WireFormatEncoder, values : Array<Any?>) {
        @Suppress("UNCHECKED_CAST")
        wireFormat.wireFormatEncode(encoder, metadata.index, metadata.name, values[metadata.index] as T?)
    }

    fun decode(decoder: WireFormatDecoder<*>, values:Array<Any?>) {
        val value = wireFormat.wireFormatDecode(decoder, metadata.index, metadata.name)
        values[metadata.index] = if (metadata.isNullable || metadata.hasDefault) value else checkNotNull(value)
    }

}