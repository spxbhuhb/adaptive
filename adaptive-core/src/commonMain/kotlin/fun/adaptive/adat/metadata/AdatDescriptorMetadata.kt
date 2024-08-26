/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat.metadata

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.wireformat.WireFormat
import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.WireFormatEncoder

@Adat
data class AdatDescriptorMetadata(
    val name: String,
    val parameters: String
) {

    fun asBoolean() = parameters.toBooleanStrict()

    fun asInt() = parameters.toInt()

    companion object : WireFormat<AdatDescriptorMetadata> {

        override val wireFormatName: String
            get() = "fun.adaptive.adat.metadata.AdatDescriptorMetadata"

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