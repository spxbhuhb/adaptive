package `fun`.adaptive.adat.visitor

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import kotlin.collections.component1
import kotlin.collections.component2

abstract class AdatClassVisitor<out R, in D> {

    abstract fun visitInstance(instance: AdatClass, data: D): R

    open fun visitProperty(instance : AdatClass, value: Any?, metadata: AdatPropertyMetadata, data: D) : Any? =
        when (value) {
            is AdatClass -> visitInstance(value, data)
            is List<*> -> value.map { item -> (item as? AdatClass)?.let { visitInstance(it, data) } ?: item }
            is Set<*> -> value.map { item -> (item as? AdatClass)?.let { visitInstance(it, data) } ?: item }.toSet()
            is Map<*, *> -> {
                val newMap = mutableMapOf<Any?, Any?>()
                value.forEach { (k, v) ->
                    val newK = if (k is AdatClass) visitInstance(k, data) else k
                    val newV = if (v is AdatClass) visitInstance(v, data) else v
                    newMap[newK] = newV
                }
                newMap.toMap()
            }
            is Array<*> -> throw UnsupportedOperationException("Array is not supported by AdatClassVisitor")
            else -> value
        }

}