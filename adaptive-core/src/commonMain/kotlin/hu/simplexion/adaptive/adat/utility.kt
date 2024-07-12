/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat

import hu.simplexion.adaptive.adat.metadata.AdatPropertyMetadata

fun AdatClass<*>.diff(other : AdatClass<*>) : List<AdatDiffItem> {
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

/**
 * Combine [this] and [other] so that the result contains:
 *
 * - the value from [this] if it equals to the value from [other]
 * - the sensible default if the value from [this] does not equal to the value from [other]
 *
 * @return  an instance of [T] that contains the combined values
 */
fun <T : AdatClass<T>> T.and(other: T): AdatClass<T> {
    val properties = this.getMetadata().properties
    val values = arrayOfNulls<Any?>(properties.size)

    for (property in properties) {
        val index = property.index
        val v1 = this.getValue(index)
        val v2 = other.getValue(index)
        if (v1 == v2) {
            values[index] = v1
        } else {
            values[index] = sensibleDefault(property)
        }
    }

    return adatCompanion.newInstance(values)
}

fun sensibleDefault(property: AdatPropertyMetadata): Any? {
    TODO()
}
