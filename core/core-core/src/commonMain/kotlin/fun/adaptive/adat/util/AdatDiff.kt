package `fun`.adaptive.adat.util

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat

class AdatDiff(
    val items: List<AdatDiffItem>
) : AdatClass {

    fun isEmpty() = items.isEmpty()
    fun isNotEmpty() = items.isNotEmpty()

    // --------------------------------------------------------------------------------
    // AdatClass overrides
    // --------------------------------------------------------------------------------

    override val adatCompanion: AdatCompanion<AdatDiff>
        get() = AdatDiff

    override fun equals(other: Any?): Boolean = adatEquals(other)
    override fun hashCode(): Int = adatHashCode()
    override fun toString(): String = adatToString()

    override fun genGetValue(index: Int): Any =
        when (index) {
            0 -> items
            else -> throw IndexOutOfBoundsException()
        }

    companion object : AdatCompanion<AdatDiff> {

        override val wireFormatName: String
            get() = "fun.adaptive.adat.util.AdatDiff"

        override val adatMetadata = AdatClassMetadata(
            version = 1,
            name = wireFormatName,
            flags = AdatClassMetadata.IMMUTABLE,
            properties = listOf(
                AdatPropertyMetadata("items", 0, 0, "Lkotlin.collections.List<Lfun.adaptive.adat.util.AdatDiffKind;>;")
            )
        )

        override val adatWireFormat: AdatClassWireFormat<AdatDiff>
            get() = AdatClassWireFormat(this, adatMetadata)

        override fun newInstance(values: Array<Any?>): AdatDiff {
            @Suppress("UNCHECKED_CAST")
            return AdatDiff(
                items = values[0] as List<AdatDiffItem>
            )
        }

        /**
         * Calculates the difference between two adat instances. The instances do not have to be
         * the same class.
         */

        fun diff(first: AdatClass, second: AdatClass): AdatDiff {
            val firstProps = first.getMetadata().properties
            val secondProps = second.getMetadata().properties

            val differences = mutableListOf<AdatDiffItem>()

            val map1 = firstProps.associateBy { it.name }
            val map2 = secondProps.associateBy { it.name }

            val allKeys = map1.keys.union(map2.keys)

            for (key in allKeys) {
                val item1 = map1[key]
                val item2 = map2[key]

                when {
                    item1 == null -> AdatDiffItem(AdatDiffKind.MissingFromThis, key, null)
                    item2 == null -> AdatDiffItem(AdatDiffKind.MissingFromOther, key, null)
                    item1.index != item2.index -> AdatDiffItem(AdatDiffKind.IndexDiff, key, null)
                    item1.signature != item2.signature -> AdatDiffItem(AdatDiffKind.SignatureDiff, key, item1.index)
                    first.getValue(item1.index) != second.getValue(item2.index) -> AdatDiffItem(AdatDiffKind.ValueDiff, key, item1.index)
                    else -> null
                }?.also {
                    differences.add(it)
                }
            }

            return AdatDiff(differences)
        }
    }
}