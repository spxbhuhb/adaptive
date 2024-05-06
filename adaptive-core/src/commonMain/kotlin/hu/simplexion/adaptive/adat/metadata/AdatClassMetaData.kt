/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat.metadata

import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder
import hu.simplexion.adaptive.wireformat.builtin.ListWireFormat

data class AdatClassMetaData<T>(
    val version: Int = 1,
    val name: String,
    val properties: List<AdatPropertyMetaData>
) {

    companion object : WireFormat<AdatClassMetaData<*>> {

        override fun wireFormatEncode(encoder: WireFormatEncoder, value: AdatClassMetaData<*>): WireFormatEncoder {
            encoder
                .int(1, "version", value.version)
                .string(2, "name", value.name)
                .instance(3, "properties", value.properties, ListWireFormat(AdatPropertyMetaData))
            return encoder
        }

        override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): AdatClassMetaData<*> {
            check(decoder != null)
            @Suppress("UNCHECKED_CAST")
            return AdatClassMetaData<Any?>(
                decoder.int(1, "version"),
                decoder.string(2, "name"),
                decoder.instance(3, "properties", ListWireFormat(AdatPropertyMetaData)) as List<AdatPropertyMetaData>,
            )
        }

    }

}