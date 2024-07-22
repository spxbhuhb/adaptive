/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat

import hu.simplexion.adaptive.adat.metadata.AdatClassMetadata
import hu.simplexion.adaptive.adat.metadata.AdatPropertyMetadata
import hu.simplexion.adaptive.wireformat.WireFormatProvider
import hu.simplexion.adaptive.wireformat.toJson


/**
 * Gets an adat class stored somewhere in this one based on [path]. Path
 * contains name of a properties to walk down.
 *
 * @throws IllegalArgumentException  when the path is invalid
 */
fun AdatClass<*>.resolve(path: List<String>): AdatClass<*> {

    var sub: Any? = this

    for (propertyName in path) {
        require(sub is AdatClass<*>) { "cannot set value for $path in ${getMetadata().name}" }
        sub = sub.getValue(propertyName)
    }

    require(sub is AdatClass<*>) { "cannot set value for $path in ${getMetadata().name}" }

    return sub
}

/**
 * Creates a deep copy of [this]. The copy is fully independent of the original, any
 * changes made on it will not change the original in any ways.
 *
 * **Mutable collections are not supported yet.** See: [Adat problems and improvements #35](https://github.com/spxbhuhb/adaptive/issues/35)
 *
 * @throws  IllegalArgumentException  In case it is not possible to make a deep copy.
 */
fun <A : AdatClass<A>> A.deepCopy(replace: AdatChange? = null): A {
    @Suppress("UNCHECKED_CAST")
    return this.genericDeepCopy(replace) as A
}

private fun AdatClass<*>.genericDeepCopy(replace: AdatChange?): AdatClass<*> {
    val properties = getMetadata().properties
    val values = arrayOfNulls<Any?>(properties.size)

    val replaceName = replace?.path?.first()

    for (property in properties) {
        val index = property.index
        values[index] = getValue(index)

        if (replaceName == property.name) {
            if (replace.path.size == 1) {
                values[index] = replace.value
            } else {
                values[index] = (getValue(index) as AdatClass<*>).genericDeepCopy(replace.next())
            }
            continue
        }

        val value = getValue(index)

        values[index] = when {
            property.hasImmutableValue -> value
            value is AdatClass<*> -> value.genericDeepCopy(null)
            else -> throw IllegalArgumentException("cannot deep copy ${getMetadata().name}.${property.name} ${getMetadata().toJson(AdatClassMetadata).decodeToString()}")
        }
    }

    return adatCompanion.newInstance(values)
}

/**
 * Calculates the difference between two adat instances. The instances do not have to be
 * same class.
 */
fun AdatClass<*>.diff(other: AdatClass<*>): List<AdatDiffItem> {
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

fun <A : AdatClass<A>> AdatClass<*>.encode(): ByteArray {
    @Suppress("UNCHECKED_CAST")
    this as A
    return WireFormatProvider.encode(this, this.adatCompanion.adatWireFormat)
}

fun <A : AdatClass<A>> ByteArray.decode(companion: AdatCompanion<A>): A =
    WireFormatProvider.decode(this, companion.adatWireFormat)