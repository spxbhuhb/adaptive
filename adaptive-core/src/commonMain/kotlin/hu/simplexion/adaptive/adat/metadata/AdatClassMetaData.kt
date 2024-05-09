/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat.metadata

import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder
import hu.simplexion.adaptive.wireformat.builtin.ListWireFormat

data class AdatClassMetaData<T>(
    val version: Int = 1,
    val name: String,
    val properties: List<AdatPropertyMetaData>
) {

    companion object : WireFormat<AdatClassMetaData<*>> {

        override fun wireFormatEncode(encoder: WireFormatEncoder, value: AdatClassMetaData<*>): WireFormatEncoder {
            encoder
                .int(1, "version", value.version)
                .string(2, "name", value.name)
                .instance(3, "properties", value.properties, ListWireFormat(AdatPropertyMetaData))
            return encoder
        }

        override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): AdatClassMetaData<*> {
            check(decoder != null)
            @Suppress("UNCHECKED_CAST")
            return AdatClassMetaData<Any?>(
                decoder.int(1, "version"),
                decoder.string(2, "name"),
                decoder.instance(3, "properties", ListWireFormat(AdatPropertyMetaData)) as List<AdatPropertyMetaData>,
            )
        }

        fun <T> decodeFromString(metaData: String): AdatClassMetaData<T> {

            // Would be more elegant to use AdatClassMetaData directly, but I can't figure
            // out how to configure Gradle dependencies properly. Shadowjar is needed to
            // have coroutines and datetime but shadowjar colludes with the normal lib.
            // This will be smaller anyway.

            // So, the format I'll use:
            // <version>/<className>/propertyName1/propertyIndex1/propertySignature1/...

            // Pair of this is in the kotlin plugin, in AdatMetaDataTransform

            val parts = metaData.split('/')

            val properties = mutableListOf<AdatPropertyMetaData>()
            var index = 2

            while (index < parts.size) {
                properties += AdatPropertyMetaData(
                    parts[index ++],
                    parts[index ++].toInt(),
                    parts[index ++]
                )
            }

            return AdatClassMetaData(
                parts[0].toInt(),
                parts[1],
                properties
            )
        }
    }

}