/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat.wireformat

import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.AdatCompanion
import hu.simplexion.adaptive.adat.metadata.AdatClassMetadata
import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder

/**
 * WireFormat generated from [AdatClassMetadata].
 *
 * [propertyWireFormats] is lazy to support cross-references between Adat classes. Adat
 * companions create their WireFormat when the companion is initialized and register
 * themselves in the `WireFormatRegistry` after. When they contain another Adat
 * class it is possible that the companion of that class is not loaded yet, resulting
 * the [toPropertyWireFormat] call to fail with a missing WireFormat.
 */
class AdatClassWireFormat<A : AdatClass<A>>(
    val companion: AdatCompanion<A>,
    val metadata: AdatClassMetadata<A>
) : WireFormat<A> {

    override val wireFormatName
        get() = companion.wireFormatName

    val propertyWireFormats by lazy { metadata.properties.map { it.toPropertyWireFormat() } }

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: A): WireFormatEncoder {
        for (propertyWireFormat in propertyWireFormats) {
            propertyWireFormat.encode(encoder, value)
        }
        return encoder
    }

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): A {
        val value = arrayOfNulls<Any?>(propertyWireFormats.size)

        for (propertyWireFormat in propertyWireFormats) {
            propertyWireFormat.decode(decoder as WireFormatDecoder<*>, value)
        }

        return companion.newInstance(value)
    }

}