/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat

import hu.simplexion.adaptive.adat.metadata.AdatClassMetaData
import hu.simplexion.adaptive.adat.metadata.AdatPropertyMetaData
import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder
import hu.simplexion.adaptive.wireformat.json.JsonWireFormatDecoder
import hu.simplexion.adaptive.wireformat.protobuf.ProtoWireFormatDecoder

interface AdatCompanion<S : AdatClass<S>> : WireFormat<S> {

    val adatMetaData : AdatClassMetaData<S>

    val adatWireFormat : WireFormat<S>

    fun newInstance(adatValues: Array<Any?>): S

    fun fromJson(byteArray: ByteArray): S =
        JsonWireFormatDecoder(byteArray).asInstance(adatWireFormat)

    fun fromProto(byteArray: ByteArray): S =
        ProtoWireFormatDecoder(byteArray).asInstance(adatWireFormat)

    fun decodeMetaData(metaData: String) : AdatClassMetaData<S> {

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
                parts[index++],
                parts[index++].toInt(),
                parts[index++]
            )
        }

        return AdatClassMetaData(
            parts[0].toInt(),
            parts[1],
            properties
        )
    }

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: S): WireFormatEncoder =
        adatWireFormat.wireFormatEncode(encoder, value)

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): S =
        adatWireFormat.wireFormatDecode(source, decoder)

}
