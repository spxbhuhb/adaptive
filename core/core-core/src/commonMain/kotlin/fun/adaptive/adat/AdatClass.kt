/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat

import `fun`.adaptive.adat.api.storeOrNull
import `fun`.adaptive.adat.descriptor.InstanceValidationResult
import `fun`.adaptive.adat.visitor.AdatClassTransformer
import `fun`.adaptive.adat.visitor.AdatClassVisitor
import `fun`.adaptive.foundation.binding.AdaptivePropertyProvider
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.utility.pluginGenerated

interface AdatClass : AdaptivePropertyProvider {

    var adatContext: AdatContext<*>?
        get() = null
        set(v) = unsupported(v)

    val adatCompanion: AdatCompanion<*>
        get() = pluginGenerated()

    fun descriptor() = Unit

    fun adatToString(): String {
        val content = mutableListOf<String>()
        getMetadata().properties.forEach { prop ->
            val value = getValue(prop.index)
            val text = when (value) {
                is Array<*> -> value.contentDeepToString()
                else -> value.toString()
            }
            content.add("${prop.name}=${text}")
        }
        return (this::class.simpleName ?: "<anonymous>") + "(" + content.joinToString(", ") + ")"
    }

    fun adatEquals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (this::class != other::class) return false
        if (other !is AdatClass) return false

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

    fun adatIndexOf(name: String): Int =
        getMetadata().properties.first { it.name == name }.index

    fun adatNameOf(index: Int): String =
        getMetadata().properties.first { it.index == index }.name

    fun getValue(name: String) =
        genGetValue(adatIndexOf(name))

    fun getValue(index: Int): Any? =
        genGetValue(index)

    fun genGetValue(index: Int): Any? {
        pluginGenerated()
    }

    fun setValue(name: String, value: Any?) {
        val store = storeOrNull<AdatClass>()
        if (store == null) {
            genSetValue(adatIndexOf(name), value)
        } else {
            store.update(this, arrayOf(name), value)
        }
    }

    fun setValue(index: Int, value: Any?) {
        val store = storeOrNull<AdatClass>()
        if (store == null) {
            genSetValue(index, value)
        } else {
            store.update(this, arrayOf(adatNameOf(index)), value)
        }
    }

    fun genSetValue(index: Int, value: Any?) {
        pluginGenerated()
    }

    fun getMetadata() =
        adatCompanion.adatMetadata

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

    // --------------------------------------------------------------------------------
    // Visit and transform
    // --------------------------------------------------------------------------------

    fun <D> accept(visitor: AdatClassVisitor<Unit, D>, data: D) {
        visitor.visitInstance(this, data)
    }

    fun <D> acceptChildren(visitor: AdatClassVisitor<Unit, D>, data: D) {

    }

    fun <D> transform(transformer: AdatClassTransformer<D>, data: D): AdatClass? {
        return transformer.visitInstance(this, data)
    }

    fun <D> transformChildren(transformer: AdatClassTransformer<D>, data: D): AdatClass {
        val properties = getMetadata().properties
        val newValues = arrayOfNulls<Any?>(properties.size)

        for (property in properties) {
            newValues[property.index] = transformer.visitProperty(this, getValue(property.index), property, data)
        }

        return adatCompanion.newInstance(newValues) as AdatClass
    }

    // --------------------------------------------------------------------------------
    // Validation
    // --------------------------------------------------------------------------------

    fun validate(): InstanceValidationResult {

        val result = InstanceValidationResult()

        for (descriptorSet in adatCompanion.adatDescriptors) {
            val value = genGetValue(descriptorSet.property.index)
            for (descriptor in descriptorSet.descriptors) {
                descriptor.validate(this, value, descriptorSet.property, result)
            }
        }

        return result
    }

    /**
     * Check if the Adat instance is invalid.
     *
     * @return  true if the instance is invalid, false otherwise
     */
    fun isNotValid(): Boolean =
        ! isValid()

    /**
     * Check if the Adat instance is valid.
     *
     * @return  true if the instance is valid, false otherwise
     */
    fun isValid(): Boolean {
        adatContext?.validationResult?.isValid?.let { return it }
        return validate().isValid
    }

    /**
     * Check if the property selected by [name] has any validation errors.
     *
     * This call is meaningful only after the `validation` function of the
     * Adat instance is called. Built-in producers such as `copyStore`, `autoInstance`
     * and `autoList` call `validate` automatically when the data changes.
     *
     * @return  true if the data is valid, false otherwise
     */
    fun isValid(name: String): Boolean =
        isValid(arrayOf(name))

    /**
     * Check if the property selected by [path] has any validation errors.
     *
     * This call is meaningful only after the `validation` function of the
     * Adat instance is called. Built-in producers such as `copyStore`, `autoInstance`
     * and `autoList` call `validate` automatically when the data changes.
     *
     * **TODO** Deep path support (AdatClass.isValid)
     *
     * @return  true if the data is valid, false otherwise
     */
    fun isValid(path: Array<String>): Boolean {
        check(path.size == 1) { "only simple paths are supported" }
        val result = adatContext?.validationResult?.failedConstraints?.filter { it.property?.name == path[0] }
        return (result == null || result.isEmpty())
    }
}