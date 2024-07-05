/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat

import hu.simplexion.adaptive.adat.metadata.AdatClassMetaData
import hu.simplexion.adaptive.utility.pluginGenerated
import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder
import hu.simplexion.adaptive.wireformat.json.JsonWireFormatDecoder
import hu.simplexion.adaptive.wireformat.protobuf.ProtoWireFormatDecoder

interface AdatCompanion<A : AdatClass<A>> : WireFormat<A> {

    val adatMetaData: AdatClassMetaData<A>
        get() = pluginGenerated()

    val adatWireFormat: WireFormat<A>
        get() = pluginGenerated()

    fun decodeMetaData(metaData: String): AdatClassMetaData<A> =
        AdatClassMetaData.decodeFromString(metaData)

    fun newInstance(): A {
        pluginGenerated()
    }

    fun fromJson(byteArray: ByteArray): A =
        JsonWireFormatDecoder(byteArray).asInstance(adatWireFormat)

    fun fromProto(byteArray: ByteArray): A =
        ProtoWireFormatDecoder(byteArray).asInstance(adatWireFormat)

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: A): WireFormatEncoder =
        adatWireFormat.wireFormatEncode(encoder, value)

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): A =
        adatWireFormat.wireFormatDecode(source, decoder)

}
