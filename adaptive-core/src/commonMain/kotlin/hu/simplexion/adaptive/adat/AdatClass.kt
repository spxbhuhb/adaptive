/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat

import hu.simplexion.adaptive.wireformat.json.JsonWireFormatEncoder
import hu.simplexion.adaptive.wireformat.protobuf.ProtoWireFormatEncoder

interface AdatClass<S : AdatClass<S>> {

    val adatValues : Array<Any?>

    val adatCompanion : AdatCompanion<S>

    fun copy() = adatCompanion.newInstance(adatValues)

    fun sEquals(s1 : AdatClass<*>, s2: AdatClass<*>?): Boolean {
        if (s1 === s2) return true
        if (s2 == null) return false
        return s1.adatValues.contentEquals(s2.adatValues)
    }

    fun toJson() : ByteArray =
        @Suppress("UNCHECKED_CAST")
        JsonWireFormatEncoder().rawInstance(this as S, adatCompanion.adatWireFormat).pack()

    fun toProto() : ByteArray =
        @Suppress("UNCHECKED_CAST")
        ProtoWireFormatEncoder().rawInstance(this as S, adatCompanion.adatWireFormat).pack()

    fun int(index : Int) = adatValues[index] as Int
    fun boolean(index : Int) = adatValues[index] as Boolean
}