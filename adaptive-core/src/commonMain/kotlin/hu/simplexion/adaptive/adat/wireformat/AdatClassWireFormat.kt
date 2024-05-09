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

/**
 * WireFormat generated from [AdatClassMetaData]. Intended use of this class is to add
 * wire format to companion objects during compilation type by the compiler plugin.
 *
 * To construct and use WireFormats dynamically during runtime use [AdatValuesWireFormat].
 *
 * [propertyWireFormats] is lazy to support cross-references between Adat classes. Adat
 * companions create their WireFormat when the companion is initialized and register
 * themselves in the `WireFormatRegistry` after. When they contain another Adat
 * class it is possible that the companion of that class is not loaded yet, resulting
 * the [toPropertyWireFormat] call to fail with a missing WireFormat.
 */
class AdatClassWireFormat<T : AdatClass<T>>(
    val companion : AdatCompanion<T>,
    metadata: AdatClassMetaData<T>
) : WireFormat<T> {

    val propertyWireFormats by lazy { metadata.properties.map { it.toPropertyWireFormat() } }

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