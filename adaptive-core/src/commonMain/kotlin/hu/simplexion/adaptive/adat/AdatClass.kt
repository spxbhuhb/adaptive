/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat

import hu.simplexion.adaptive.adat.descriptor.result.InstanceValidationResult
import hu.simplexion.adaptive.foundation.binding.AdaptivePropertyProvider
import hu.simplexion.adaptive.foundation.binding.AdaptiveStateVariableBinding
import hu.simplexion.adaptive.foundation.unsupported
import hu.simplexion.adaptive.utility.pluginGenerated
import hu.simplexion.adaptive.wireformat.json.JsonWireFormatEncoder
import hu.simplexion.adaptive.wireformat.protobuf.ProtoWireFormatEncoder

interface AdatClass<A : AdatClass<A>> : AdaptivePropertyProvider {

    var adatContext: AdatContext?
        get() = null
        set(v) = unsupported(v)

    val adatCompanion: AdatCompanion<A>
        get() = pluginGenerated()

    fun isImmutable() =
        getMetadata().isImmutable

    fun description() = Unit

    fun validate() {
        if (adatContext == null) {
            adatContext = AdatContext(null)
        }
        adatContext !!.validationResult = InstanceValidationResult()
    }

    fun copy(): A {
        val properties = getMetadata().properties
        val values = arrayOfNulls<Any?>(properties.size)

        getMetadata().properties.forEach { prop ->
            values[prop.index] = getValue(prop.index)
        }

        return adatCompanion.newInstance(values)
    }

    fun adatToString(): String {
        val content = mutableListOf<String>()
        getMetadata().properties.forEach { prop ->
            content.add("${prop.name}=${getValue(prop.index)}")
        }
        return (this::class.simpleName ?: "<anonymous>") + "(" + content.joinToString(", ") + ")"
    }

    fun adatEquals(other: AdatClass<*>?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (this::class != other::class) return false

        repeat(getMetadata().properties.size) {
            if (this.getValue(it) != other.getValue(it)) return false
        }

        return true
    }

    fun adatHashCode(): Int {
        var hashCode = 0

        repeat(getMetadata().properties.size) {
            val prop = getValue(it)
            hashCode = 31 * hashCode + (prop?.hashCode() ?: 0)
        }

        return hashCode
    }

    fun toJson() : ByteArray =
        @Suppress("UNCHECKED_CAST")
        JsonWireFormatEncoder().rawInstance(this as A, adatCompanion.adatWireFormat).pack()

    fun toProto() : ByteArray =
        @Suppress("UNCHECKED_CAST")
        ProtoWireFormatEncoder().rawInstance(this as A, adatCompanion.adatWireFormat).pack()

    fun adatIndexOf(name : String) : Int =
        getMetadata().properties.first { it.name == name }.index

    fun getValue(name : String) =
        genGetValue(adatIndexOf(name))

    fun getValue(index: Int): Any? =
        genGetValue(index)

    fun genGetValue(index: Int): Any? {
        pluginGenerated()
    }

    fun setValue(name : String, value: Any?) {
        genSetValue(adatIndexOf(name), value)
    }

    fun setValue(index : Int, value: Any?) {
        genSetValue(index, value)
    }

    fun genSetValue(index: Int, value: Any?) {
        pluginGenerated()
    }

    fun getMetadata() =
        adatCompanion.adatMetadata

    fun getPropertyMetadata(name: String) =
        adatCompanion.adatMetadata.properties.first { it.name == name }

    fun getPropertyMetadata(index: Int) =
        adatCompanion.adatMetadata.properties[index]

    // FIXME AdatClass.addBinding
    override fun addBinding(binding: AdaptiveStateVariableBinding<*>) = Unit

    // FIXME AdatClass.removeBinding
    override fun removeBinding(binding: AdaptiveStateVariableBinding<*>) = Unit

    override fun getValue(path: Array<String>): Any? =
        if (path.size == 1) {
            getValue(path[0])
        } else {
            resolve(path.dropLast(1)).getValue(path.last())
        }


    override fun setValue(path: Array<String>, value: Any?) {
        if (path.size == 1) {
            setValue(path[0], value)
        } else {
            resolve(path.dropLast(1)).setValue(path.last(), value)
        }
    }


    fun invalidIndex(index: Int): Nothing {
        throw IndexOutOfBoundsException("index $index is invalid in ${getMetadata().name}")
    }

}