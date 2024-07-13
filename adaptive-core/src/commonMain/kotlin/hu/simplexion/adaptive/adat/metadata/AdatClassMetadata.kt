/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat.metadata

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.descriptor.AdatDescriptorImpl
import hu.simplexion.adaptive.adat.descriptor.DefaultDescriptorFactory
import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder
import hu.simplexion.adaptive.wireformat.builtin.ListWireFormat
import hu.simplexion.adaptive.wireformat.fromJson

@Adat
data class AdatClassMetadata<T>(
    val version: Int = 1,
    val name: String,
    val flags: Int,
    val properties: List<AdatPropertyMetadata>
) {

    val isImmutable
        get() = (flags and ADAT_CLASS_FLAG_IMMUTABLE) != 0

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

        const val ADAT_CLASS_FLAG_IMMUTABLE = 1

        override val wireFormatName: String
            get() = "hu.simplexion.adaptive.adat.metadata.AdatClassMetadata"

        override fun wireFormatEncode(encoder: WireFormatEncoder, value: AdatClassMetadata<*>): WireFormatEncoder {
            encoder
                .int(1, "v", value.version)
                .string(2, "n", value.name)
                .int(3, "f", value.flags)
                .instance(4, "p", value.properties, ListWireFormat(AdatPropertyMetadata))
            return encoder
        }

        override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): AdatClassMetadata<*> {
            check(decoder != null)
            @Suppress("UNCHECKED_CAST")
            return AdatClassMetadata<Any?>(
                decoder.int(1, "v"),
                decoder.string(2, "n"),
                decoder.int(3, "f"),
                decoder.instance(4, "p", ListWireFormat(AdatPropertyMetadata)) as List<AdatPropertyMetadata>,
            )
        }

        @Suppress("UNCHECKED_CAST")
        fun <T> decodeFromString(a: String): AdatClassMetadata<T> =
            a.encodeToByteArray().fromJson(this) as AdatClassMetadata<T>

    }

}