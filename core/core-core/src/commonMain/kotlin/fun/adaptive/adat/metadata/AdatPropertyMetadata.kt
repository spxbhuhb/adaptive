/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat.metadata

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.descriptor.AdatDescriptor
import `fun`.adaptive.adat.descriptor.AdatDescriptorSet
import `fun`.adaptive.adat.descriptor.ConstraintFail
import `fun`.adaptive.adat.descriptor.InstanceValidationResult
import `fun`.adaptive.adat.descriptor.kotlin.string.StringSecret
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat

@Adat
class AdatPropertyMetadata(
    val name: String,
    val index: Int,
    val flags: Int,
    val signature: String,
    val descriptors: List<AdatDescriptorMetadata> = emptyList(),
) : AdatClass {

    /**
     * True when the property is mutable:
     *
     * - it is read-write **AND** has a backing field
     * - **OR** the value of the property is mutable
     */
    val isMutableProperty
        inline get() = ! isImmutableProperty

    /**
     * True when the property is immutable:
     *
     * - it is read-only (declared as `val`)
     * - **AND**
     *   - the value of the property is immutable
     *   - **OR** it does not have a backing field
     */
    val isImmutableProperty
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
     * Use [isMutableProperty] or [isImmutableProperty] to check for actual mutability.
     */
    val hasImmutableValue
        get() = (flags and IMMUTABLE_VALUE) != 0

    /**
     * True when the property value is an adat class.
     */
    val isAdatClass
        get() = (flags and ADAT_CLASS) != 0

    /**
     * True when the property is nullable.
     */
    val isNullable
        get() = (flags and NULLABLE) != 0

    /**
     * True when the property has a default value.
     */
    val hasDefault
        get() = (flags and HAS_DEFAULT) != 0

    fun isSecret(descriptors : Array<AdatDescriptorSet>) =
        descriptors.first { it.property.name == name }.descriptors.filterIsInstance<StringSecret>().firstOrNull()?.isSecret == true

    fun fail(result : InstanceValidationResult, descriptor : AdatDescriptor) {
        result.failedConstraints += ConstraintFail(this, descriptor.metadata)
    }

    // --------------------------------------------------------------------------------
    // AdatClass overrides
    // --------------------------------------------------------------------------------

    override val adatCompanion: AdatCompanion<AdatPropertyMetadata>
        get() = AdatPropertyMetadata

    override fun equals(other: Any?): Boolean = adatEquals(other)
    override fun hashCode(): Int = adatHashCode()
    override fun toString(): String = adatToString()

    override fun genGetValue(index: Int): Any? =
        when (index) {
            0 -> name
            1 -> this.index
            2 -> flags
            3 -> signature
            4 -> descriptors
            else -> throw IndexOutOfBoundsException()
        }

    companion object : AdatCompanion<AdatPropertyMetadata> {

        const val VAL = 1
        const val IMMUTABLE_VALUE = 2
        const val ADAT_CLASS = 4
        const val NULLABLE = 8
        const val HAS_DEFAULT = 16

        override val wireFormatName: String
            get() = "fun.adaptive.adat.metadata.AdatPropertyMetadata"

        override val adatMetadata = AdatClassMetadata(
            version = 1,
            name = wireFormatName,
            flags = 0,
            properties = listOf(
                AdatPropertyMetadata("name", 0, 0, "T"),
                AdatPropertyMetadata("index", 1, 0, "I"),
                AdatPropertyMetadata("flags", 2, 0, "I"),
                AdatPropertyMetadata("signature", 3, 0, "T"),
                AdatPropertyMetadata("descriptors", 4, 0, "Lkotlin.collections.List<Lfun.adaptive.adat.metadata.AdatDescriptorMetadata;>;")
            )
        )

        override val adatWireFormat: AdatClassWireFormat<AdatPropertyMetadata>
            get() = AdatClassWireFormat(this, adatMetadata)

        override fun newInstance(values: Array<Any?>): AdatPropertyMetadata {
            @Suppress("UNCHECKED_CAST")
            return AdatPropertyMetadata(
                name = values[0] as String,
                index = values[1] as Int,
                flags = values[2] as Int,
                signature = values[3] as String,
                descriptors = values[4] as List<AdatDescriptorMetadata>
            )
        }
    }
}