/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat

import `fun`.adaptive.adat.descriptor.AdatDescriptorSet
import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.utility.pluginGenerated
import `fun`.adaptive.wireformat.WireFormat
import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.WireFormatEncoder
import `fun`.adaptive.wireformat.json.JsonWireFormatDecoder
import `fun`.adaptive.wireformat.protobuf.ProtoWireFormatDecoder

interface AdatCompanion<A : AdatClass<A>> : WireFormat<A> {

    val adatMetadata: AdatClassMetadata<A>
        get() = pluginGenerated()

    val adatWireFormat: AdatClassWireFormat<A>
        get() = pluginGenerated()

    val adatDescriptors: Array<AdatDescriptorSet>
        get() = pluginGenerated()

    fun decodeMetadata(a: String): AdatClassMetadata<A> =
        AdatClassMetadata.decodeFromString(a)

    fun generateDescriptors() : Array<AdatDescriptorSet> =
        adatMetadata.generateDescriptors()

    fun newInstance(): A {
        pluginGenerated()
    }

    fun newInstance(values: Array<Any?>): A {
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
