/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat

import `fun`.adaptive.wireformat.WireFormat
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.json.JsonWireFormatProvider

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

fun <A : AdatClass> A.encodeToJson(): String =
    @Suppress("UNCHECKED_CAST")
    JsonWireFormatProvider().encode(this, this.adatCompanion.adatWireFormat as WireFormat<A>).decodeToString()

fun <A : AdatClass> AdatCompanion<A>.decodeFromJson(json: String) =
    @Suppress("UNCHECKED_CAST")
    JsonWireFormatProvider().decode(json.encodeToByteArray(), adatWireFormat as WireFormat<A>)

// TODO I'm not happy with AdatClass.encode and decode (unchecked cast)
fun <A : AdatClass> A.encode(wireFormatProvider: WireFormatProvider): ByteArray {
    @Suppress("UNCHECKED_CAST")
    return wireFormatProvider.encode(this, this.adatCompanion.adatWireFormat as WireFormat<A>)
}

fun <A : AdatClass> ByteArray.decode(wireFormatProvider: WireFormatProvider, companion: AdatCompanion<A>): A =
    wireFormatProvider.decode(this, companion.adatWireFormat)

