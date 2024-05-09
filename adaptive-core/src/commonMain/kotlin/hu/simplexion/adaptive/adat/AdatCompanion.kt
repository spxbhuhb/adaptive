/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat

import hu.simplexion.adaptive.adat.metadata.AdatClassMetaData
import hu.simplexion.adaptive.adat.metadata.AdatPropertyMetaData
import hu.simplexion.adaptive.utility.pluginGenerated
import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder
import hu.simplexion.adaptive.wireformat.json.JsonWireFormatDecoder
import hu.simplexion.adaptive.wireformat.protobuf.ProtoWireFormatDecoder

interface AdatCompanion<S : AdatClass<S>> : WireFormat<S> {

    val adatMetaData: AdatClassMetaData<S>
        get() = pluginGenerated()

    val adatWireFormat: WireFormat<S>
        get() = pluginGenerated()

    fun decodeMetaData(metaData: String) : AdatClassMetaData<S> =
        AdatClassMetaData.decodeFromString(metaData)

    fun newInstance(adatValues: Array<Any?>): S {
        pluginGenerated()
    }

    fun fromJson(byteArray: ByteArray): S =
        JsonWireFormatDecoder(byteArray).asInstance(adatWireFormat)

    fun fromProto(byteArray: ByteArray): S =
        ProtoWireFormatDecoder(byteArray).asInstance(adatWireFormat)

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: S): WireFormatEncoder =
        adatWireFormat.wireFormatEncode(encoder, value)

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): S =
        adatWireFormat.wireFormatDecode(source, decoder)

}
