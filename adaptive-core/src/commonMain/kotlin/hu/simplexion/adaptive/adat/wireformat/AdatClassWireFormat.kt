/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat.wireformat

import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.metadata.AdatClassMetaData
import hu.simplexion.adaptive.adat.AdatCompanion
import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder

class AdatClassWireFormat<T : AdatClass<T>>(
    val companion : AdatCompanion<T>,
    metadata: AdatClassMetaData<T>
) : WireFormat<T> {

    val propertyWireFormats = metadata.properties.map { it.toPropertyWireFormat() }

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: T): WireFormatEncoder {
        val values = value.adatValues
        for (propertyWireFormat in propertyWireFormats) {
            propertyWireFormat.encode(encoder, values)
        }
        return encoder
    }

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): T {
        val values = arrayOfNulls<Any?>(propertyWireFormats.size)

        for (propertyWireFormat in propertyWireFormats) {
            propertyWireFormat.decode(decoder as WireFormatDecoder<*>, values)
        }

        return companion.newInstance(values)
    }

}