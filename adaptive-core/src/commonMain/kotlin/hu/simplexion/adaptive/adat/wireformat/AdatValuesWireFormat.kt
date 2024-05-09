/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat.wireformat

import hu.simplexion.adaptive.adat.metadata.AdatClassMetaData
import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder

/**
 * WireFormat generated from [AdatClassMetaData]. Intended use case of this class is to construct and use
 * WireFormats dynamically during runtime. In contrast, [AdatClassWireFormat] is meant for the compiler plugin
 * to be added to the code during compilation time.
 */
class AdatValuesWireFormat(
    metadata: String
) : WireFormat<Array<Any?>> {

    val propertyWireFormats =
        AdatClassMetaData.decodeFromString<Any>(metadata)
            .properties
            .map { it.toPropertyWireFormat() }

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Array<Any?>): WireFormatEncoder {
        for (propertyWireFormat in propertyWireFormats) {
            propertyWireFormat.encode(encoder, value)
        }
        return encoder
    }

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Array<Any?> {
        val value = arrayOfNulls<Any?>(propertyWireFormats.size)

        for (propertyWireFormat in propertyWireFormats) {
            propertyWireFormat.decode(decoder as WireFormatDecoder<*>, value)
        }

        return value
    }

}