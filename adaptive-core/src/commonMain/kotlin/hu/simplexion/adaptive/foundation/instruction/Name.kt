/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation.instruction

import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder

fun name(name : String) = Name(name)

/**
 * Give a name to a fragment. Trace checks if the fragment has this instruction
 * and uses it instead of the class name if so.
 */
data class Name(val name: String) : AdaptiveInstruction {

    constructor() : this("<anonymous>")

    companion object : WireFormat<Name> {

        val ANONYMOUS = Name("<anonymous>")

        override val wireFormatName: String
            get() = "hu.simplexion.adaptive.adat.metadata.AdatPropertyMetadata"

        override fun wireFormatEncode(encoder: WireFormatEncoder, value: Name): WireFormatEncoder {
            encoder
                .string(1, "name", value.name)
            return encoder
        }

        override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Name {
            check(decoder != null)
            return Name(decoder.string(1, "name"))
        }

    }

}