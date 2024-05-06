/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat.metadata

import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder

data class AdatPropertyMetaData(
    val name: String,
    val index: Int,
    val signature: String
) {

    companion object : WireFormat<AdatPropertyMetaData> {

        override fun wireFormatEncode(encoder: WireFormatEncoder, value: AdatPropertyMetaData): WireFormatEncoder {
            encoder
                .string(1, "name", value.name)
                .int(2, "index", value.index)
                .string(3, "signature", value.signature)
            return encoder
        }

        override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): AdatPropertyMetaData {
            check(decoder != null)
            return AdatPropertyMetaData(
                decoder.string(1, "name"),
                decoder.int(2, "index"),
                decoder.string(3, "signature")
            )
        }

    }
}