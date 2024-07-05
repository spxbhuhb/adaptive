/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat.metadata

import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder

data class AdatDescriptorMetadata(
    val name: String,
    val parameters: String
) {

    companion object : WireFormat<AdatDescriptorMetadata> {

        override fun wireFormatEncode(encoder: WireFormatEncoder, value: AdatDescriptorMetadata): WireFormatEncoder {
            encoder
                .string(1, "n", value.name)
                .string(2, "p", value.parameters)
            return encoder
        }

        override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): AdatDescriptorMetadata {
            check(decoder != null)
            return AdatDescriptorMetadata(
                decoder.string(1, "n"),
                decoder.string(2, "p")
            )
        }

    }
}