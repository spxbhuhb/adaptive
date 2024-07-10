/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat.metadata

import hu.simplexion.adaptive.adat.descriptor.AdatDescriptorImpl
import hu.simplexion.adaptive.adat.descriptor.DefaultDescriptorFactory
import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder
import hu.simplexion.adaptive.wireformat.builtin.ListWireFormat
import hu.simplexion.adaptive.wireformat.fromJson

data class AdatClassMetadata<T>(
    val version: Int = 1,
    val name: String,
    val properties: List<AdatPropertyMetadata>
) {

    fun generateDescriptors(): List<AdatDescriptorImpl> {
        val result = mutableListOf<AdatDescriptorImpl>()

        for (property in properties) {
            for (descriptor in property.descriptors) {
                result += DefaultDescriptorFactory.newInstance(descriptor.name, property, descriptor)
            }
        }

        return result
    }

    companion object : WireFormat<AdatClassMetadata<*>> {

        override val wireFormatName: String
            get() = "hu.simplexion.adaptive.adat.metadata.AdatClassMetadata"

        override fun wireFormatEncode(encoder: WireFormatEncoder, value: AdatClassMetadata<*>): WireFormatEncoder {
            encoder
                .int(1, "v", value.version)
                .string(2, "n", value.name)
                .instance(3, "p", value.properties, ListWireFormat(AdatPropertyMetadata))
            return encoder
        }

        override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): AdatClassMetadata<*> {
            check(decoder != null)
            @Suppress("UNCHECKED_CAST")
            return AdatClassMetadata<Any?>(
                decoder.int(1, "v"),
                decoder.string(2, "n"),
                decoder.instance(3, "p", ListWireFormat(AdatPropertyMetadata)) as List<AdatPropertyMetadata>,
            )
        }

        @Suppress("UNCHECKED_CAST")
        fun <T> decodeFromString(a: String): AdatClassMetadata<T> =
            a.encodeToByteArray().fromJson(this) as AdatClassMetadata<T>

    }

}