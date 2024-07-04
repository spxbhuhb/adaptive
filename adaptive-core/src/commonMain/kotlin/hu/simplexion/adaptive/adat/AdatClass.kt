/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat

import hu.simplexion.adaptive.foundation.binding.AdaptivePropertyProvider
import hu.simplexion.adaptive.foundation.binding.AdaptiveStateVariableBinding
import hu.simplexion.adaptive.utility.pluginGenerated
import hu.simplexion.adaptive.wireformat.json.JsonWireFormatEncoder
import hu.simplexion.adaptive.wireformat.protobuf.ProtoWireFormatEncoder

interface AdatClass<A : AdatClass<A>> : AdaptivePropertyProvider {

    val adatValues : Array<Any?>
        get() = pluginGenerated()

    val adatCompanion: AdatCompanion<A>
        get() = pluginGenerated()

    fun isValid() = true

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
        JsonWireFormatEncoder().rawInstance(this as A, adatCompanion.adatWireFormat).pack()

    fun toProto() : ByteArray =
        @Suppress("UNCHECKED_CAST")
        ProtoWireFormatEncoder().rawInstance(this as A, adatCompanion.adatWireFormat).pack()

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

    // FIXME AdatClass.addBinding
    override fun addBinding(binding: AdaptiveStateVariableBinding<*>) = Unit

    // FIXME AdatClass.removeBinding
    override fun removeBinding(binding: AdaptiveStateVariableBinding<*>) = Unit

    override fun getValue(path : Array<String>) : Any? {
        check(path.size == 1) {"nested property access is not supported yet"}
        return getValue(adatIndexOf(path[0]))
    }

    override fun setValue(path : Array<String>, value : Any?, fromBinding: AdaptiveStateVariableBinding<*>) {
        check(path.size == 1) {"nested property access is not supported yet"}
        setValue(adatIndexOf(path[0]), value)
    }

}