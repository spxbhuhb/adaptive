package `fun`.adaptive.adat.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatDiffItem
import `fun`.adaptive.adat.AdatDiffKind

/**
 * Calculates the difference between two adat instances. The instances do not have to be
 * same class.
 */
fun AdatClass.diff(other: AdatClass): List<AdatDiffItem> {
    val thisProps = this.getMetadata().properties
    val otherProps = other.getMetadata().properties

    val differences = mutableListOf<AdatDiffItem>()

    val map1 = thisProps.associateBy { it.name }
    val map2 = otherProps.associateBy { it.name }

    val allKeys = map1.keys.union(map2.keys)

    for (key in allKeys) {
        val item1 = map1[key]
        val item2 = map2[key]

        when {
            item1 == null -> AdatDiffItem(AdatDiffKind.MissingFromThis, key, null)
            item2 == null -> AdatDiffItem(AdatDiffKind.MissingFromOther, key, null)
            item1.index != item2.index -> AdatDiffItem(AdatDiffKind.IndexDiff, key, null)
            item1.signature != item2.signature -> AdatDiffItem(AdatDiffKind.SignatureDiff, key, item1.index)
            getValue(item1.index) != other.getValue(item2.index) -> AdatDiffItem(AdatDiffKind.ValueDiff, key, item1.index)
            else -> null
        }?.also {
            differences.add(it)
        }
    }

    return differences
}