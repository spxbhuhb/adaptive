/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat.metadata

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.descriptor.AdatDescriptor
import `fun`.adaptive.adat.descriptor.AdatDescriptorSet
import `fun`.adaptive.adat.descriptor.ConstraintFail
import `fun`.adaptive.adat.descriptor.InstanceValidationResult
import `fun`.adaptive.adat.descriptor.kotlin.string.StringSecret
import `fun`.adaptive.wireformat.WireFormat
import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.WireFormatEncoder
import `fun`.adaptive.wireformat.builtin.ListWireFormat

@Adat
data class AdatPropertyMetadata(
    val name: String,
    val index: Int,
    val flags: Int,
    val signature: String,
    val descriptors: List<AdatDescriptorMetadata> = emptyList(),
) {

    /**
     * True when the property is mutable:
     *
     * - it is read-write **AND** has a backing field
     * - **OR** the value of the property is mutable
     */
    val isMutable
        inline get() = ! isImmutable

    /**
     * True when the property is immutable:
     *
     * - it is read-only (declared as `val`)
     * - **AND**
     *   - the value of the property is immutable
     *   - **OR** it does not have a backing field
     */
    val isImmutable
        get() = isVal && hasImmutableValue

    /**
     * True when the property is read-only (declared as `val`, no getter).
     *
     * Note that this **DOES NOT** mean immutability, the internal properties of
     * the value can change even if the property itself is read-only.
     *
     * Use [hasImmutableValue] to check if the value itself is immutable.
     */
    val isVal
        get() = (flags and VAL) != 0

    /**
     * True when the value of the property is mutable.
     */
    val hasMutableValue
        get() = ! hasImmutableValue

    /**
     * True when the value of the property is immutable.
     *
     * Note that this **DOES NOT** mean immutability, the property value can change.
     * Use [isMutable] or [isImmutable] to check for actual mutability.
     */
    val hasImmutableValue
        get() = (flags and IMMUTABLE_VALUE) != 0

    /**
     * True when the property value is an adat class.
     */
    val isAdatClass
        get() = (flags and ADAT_CLASS) != 0

    fun isSecret(descriptors : Array<AdatDescriptorSet>) =
        descriptors.first { it.property.name == name }.descriptors.filterIsInstance<StringSecret>().firstOrNull()?.isSecret == true

    fun fail(result : InstanceValidationResult, descriptor : AdatDescriptor) {
        result.failedConstraints += ConstraintFail(this, descriptor.metadata)
    }

    companion object : WireFormat<AdatPropertyMetadata> {

        const val VAL = 1
        const val IMMUTABLE_VALUE = 2
        const val ADAT_CLASS = 4

        override val wireFormatName: String
            get() = "fun.adaptive.adat.metadata.AdatPropertyMetadata"

        override fun wireFormatEncode(encoder: WireFormatEncoder, value: AdatPropertyMetadata): WireFormatEncoder {
            encoder
                .string(1, "n", value.name)
                .int(2, "i", value.index)
                .int(3, "f", value.flags)
                .string(4, "s", value.signature)
                .instance(5, "d", value.descriptors, ListWireFormat(AdatDescriptorMetadata))
            return encoder
        }

        override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): AdatPropertyMetadata {
            check(decoder != null)
            @Suppress("UNCHECKED_CAST")
            return AdatPropertyMetadata(
                decoder.string(1, "n"),
                decoder.int(2, "i"),
                decoder.int(3, "f"),
                decoder.string(4, "s"),
                decoder.instance(5, "d", ListWireFormat(AdatDescriptorMetadata)) as List<AdatDescriptorMetadata>,
            )
        }

    }
}