/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat.metadata

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder
import hu.simplexion.adaptive.wireformat.builtin.ListWireFormat

@Adat
data class AdatPropertyMetadata(
    val name: String,
    val index: Int,
    val flags: Int,
    val signature: String,
    val descriptors: List<AdatDescriptorMetadata> = emptyList(),
) {

    companion object : WireFormat<AdatPropertyMetadata> {

        const val ADAT_PROPERTY_FLAG_VAL = 1
        const val ADAT_PROPERTY_FLAG_IMMUTABLE = 2

        override val wireFormatName: String
            get() = "hu.simplexion.adaptive.adat.metadata.AdatPropertyMetadata"

        override fun wireFormatEncode(encoder: WireFormatEncoder, value: AdatPropertyMetadata): WireFormatEncoder {
            encoder
                .string(1, "n", value.name)
                .int(2, "i", value.index)
                .int(3, "f", value.flags)
                .string(4, "s", value.signature)
                .instance(5, "d", value.descriptors, ListWireFormat(AdatDescriptorMetadata))
            return encoder
        }

        override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): AdatPropertyMetadata {
            check(decoder != null)
            @Suppress("UNCHECKED_CAST")
            return AdatPropertyMetadata(
                decoder.string(1, "n"),
                decoder.int(2, "i"),
                decoder.int(3, "f"),
                decoder.string(4, "s"),
                decoder.instance(5, "d", ListWireFormat(AdatDescriptorMetadata)) as List<AdatDescriptorMetadata>,
            )
        }

    }
}