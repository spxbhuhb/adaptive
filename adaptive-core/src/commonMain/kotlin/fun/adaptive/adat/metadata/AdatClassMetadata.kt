/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat.metadata

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.descriptor.AdatDescriptor
import `fun`.adaptive.adat.descriptor.AdatDescriptorSet
import `fun`.adaptive.adat.descriptor.DefaultDescriptorFactory
import `fun`.adaptive.wireformat.WireFormat
import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.WireFormatEncoder
import `fun`.adaptive.wireformat.builtin.ListWireFormat
import `fun`.adaptive.wireformat.fromJson

/**
 * @property  name   The fully qualified (dot separated) name of the adat class this metadata describes.
 */
@Adat
data class AdatClassMetadata<T>(
    val version: Int = 1,
    val name: String,
    val flags: Int,
    val properties: List<AdatPropertyMetadata>
) {

    /**
     * True then the class is mutable:
     *
     * - at least one property is `var` or has a getter
     * - **OR** at least one property has a mutable value
     */
    val isMutable
        inline get() = ! isImmutable

    /**
     * True then the whole class is immutable:
     *
     * - all properties are `val`
     * - **AND** all property values are immutable
     */
    val isImmutable
        get() = (flags and IMMUTABLE) != 0

    fun generateDescriptors(): Array<AdatDescriptorSet> {
        val result = mutableListOf<AdatDescriptorSet>()

        for (property in properties) {
            val propertyResult = mutableListOf<AdatDescriptor>()
            for (descriptor in property.descriptors) {
                propertyResult += DefaultDescriptorFactory.newInstance(descriptor.name, descriptor)
            }
            result += AdatDescriptorSet(property, propertyResult)
        }

        return result.toTypedArray()
    }

    operator fun get(propertyName: String): AdatPropertyMetadata =
        properties.first { it.name == propertyName }

    companion object : WireFormat<AdatClassMetadata<*>> {

        /**
         * Set then the whole class is immutable:
         *
         * - all properties are `val`
         * - all property values are immutable
         */
        const val IMMUTABLE = 1

        override val wireFormatName: String
            get() = "fun.adaptive.adat.metadata.AdatClassMetadata"

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
        fun <T> decodeFromString(a: String): AdatClassMetadata<T> {
            return a.encodeToByteArray().fromJson(this) as AdatClassMetadata<T>
        }

    }

}