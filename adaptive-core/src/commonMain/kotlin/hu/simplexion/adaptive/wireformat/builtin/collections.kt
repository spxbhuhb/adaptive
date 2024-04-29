/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.builtin

import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder
import hu.simplexion.adaptive.wireformat.WireFormatKind

class ListWireFormat<T>(
    val itemWireFormat: WireFormat<T>
) : WireFormat<List<T>> {

    override val kind: WireFormatKind
        get() = WireFormatKind.Collection

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: List<T>): WireFormatEncoder {
        encoder.items(value, itemWireFormat)
        return encoder
    }

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): List<T> {
        if (decoder == null) return emptyList()
        return decoder.items(source, itemWireFormat)
    }

}