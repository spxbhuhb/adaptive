/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat

import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.wireformat.WireFormat
import `fun`.adaptive.wireformat.WireFormatProvider

/**
 * Gets an adat class stored somewhere in this one based on [path]. Path
 * contains name of a properties to walk down.
 *
 * @throws IllegalArgumentException  when the path is invalid
 */
fun AdatClass.resolve(path: List<String>): AdatClass {

    var sub: Any? = this

    for (propertyName in path) {
        require(sub is AdatClass) { "cannot set value for $path in ${getMetadata().name}" }
        sub = sub.getValue(propertyName)
    }

    require(sub is AdatClass) { "cannot set value for $path in ${getMetadata().name}" }

    return sub
}


/**
 * Combine [this] and [other] so that the result contains:
 *
 * - the value from [this] if it equals to the value from [other]
 * - the sensible default if the value from [this] does not equal to the value from [other]
 *
 * @return  an instance of [T] that contains the combined values
 */
fun <A : AdatClass> A.and(other: A): A {
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

    @Suppress("UNCHECKED_CAST")
    return adatCompanion.newInstance(values) as A
}

fun sensibleDefault(property: AdatPropertyMetadata): Any? {
    TODO()
}

// TODO I'm not happy with AdatClass.encode and decode (unchecked cast)
fun <A : AdatClass> A.encode(wireFormatProvider: WireFormatProvider): ByteArray {
    @Suppress("UNCHECKED_CAST")
    return wireFormatProvider.encode(this, this.adatCompanion.adatWireFormat as WireFormat<A>)
}

fun <A : AdatClass> ByteArray.decode(wireFormatProvider: WireFormatProvider, companion: AdatCompanion<A>): A =
    wireFormatProvider.decode(this, companion.adatWireFormat)

