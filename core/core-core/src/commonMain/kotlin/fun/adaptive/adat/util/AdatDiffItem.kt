/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat.util

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat

class AdatDiffItem(
    val kind : AdatDiffKind,
    val path : String,
    val index : Int?
) : AdatClass {

    // --------------------------------------------------------------------------------
    // AdatClass overrides
    // --------------------------------------------------------------------------------

    override val adatCompanion: AdatCompanion<AdatDiffItem>
        get() = AdatDiffItem

    override fun equals(other: Any?): Boolean = adatEquals(other)
    override fun hashCode(): Int = adatHashCode()
    override fun toString(): String = adatToString()

    override fun genGetValue(index: Int): Any? =
        when (index) {
            0 -> kind
            1 -> path
            2 -> this.index
            else -> throw IndexOutOfBoundsException()
        }

    companion object : AdatCompanion<AdatDiffItem> {

        override val wireFormatName: String
            get() = "fun.adaptive.adat.util.AdatDiffItem"

        override val adatMetadata = AdatClassMetadata(
            version = 1,
            name = wireFormatName,
            flags = AdatClassMetadata.IMMUTABLE,
            properties = listOf(
                AdatPropertyMetadata("kind", 0, 0, "Lfun.adaptive.adat.util.AdatDiffKind;"),
                AdatPropertyMetadata("name", 1, 0, "T"),
                AdatPropertyMetadata("index", 2, 0, "I?")
            )
        )

        override val adatWireFormat: AdatClassWireFormat<AdatDiffItem>
            get() = AdatClassWireFormat(this, adatMetadata)

        override fun newInstance(values: Array<Any?>): AdatDiffItem {
            @Suppress("UNCHECKED_CAST")
            return AdatDiffItem(
                kind = values[0] as AdatDiffKind,
                path = values[1] as String,
                index = values[2] as Int?
            )
        }

    }
}