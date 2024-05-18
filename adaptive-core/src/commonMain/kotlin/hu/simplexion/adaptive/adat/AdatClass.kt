/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat

import hu.simplexion.adaptive.utility.pluginGenerated
import hu.simplexion.adaptive.wireformat.json.JsonWireFormatEncoder
import hu.simplexion.adaptive.wireformat.protobuf.ProtoWireFormatEncoder

interface AdatClass<S : AdatClass<S>> {

    val adatValues : Array<Any?>
        get() = pluginGenerated()

    val adatCompanion : AdatCompanion<S>
        get() = pluginGenerated()

    fun copy() = adatCompanion.newInstance(adatValues.copyOf())

    fun adatToString() =
        this::class.simpleName

    fun adatEquals(s2: AdatClass<*>?): Boolean {
        if (this === s2) return true
        if (s2 == null) return false
        return adatValues.contentEquals(s2.adatValues)
    }

    fun adatHashCode() : Int =
        adatValues.contentHashCode()

    fun toJson() : ByteArray =
        @Suppress("UNCHECKED_CAST")
        JsonWireFormatEncoder().rawInstance(this as S, adatCompanion.adatWireFormat).pack()

    fun toProto() : ByteArray =
        @Suppress("UNCHECKED_CAST")
        ProtoWireFormatEncoder().rawInstance(this as S, adatCompanion.adatWireFormat).pack()

    fun adatIndexOf(name : String) : Int =
        getMetadata().properties.first { it.name == name }.index

    fun getValue(name : String) =
        getValue(adatIndexOf(name))

    fun getValue(index : Int) = adatValues[index]

    fun setValue(name : String, value: Any?) {
        setValue(adatIndexOf(name), value)
    }

    fun setValue(index : Int, value: Any?) {
        adatValues[index] = value
    }

    fun int(index : Int) = adatValues[index] as Int

    fun boolean(index : Int) = adatValues[index] as Boolean

    fun getMetadata() =
        adatCompanion.adatMetaData

}