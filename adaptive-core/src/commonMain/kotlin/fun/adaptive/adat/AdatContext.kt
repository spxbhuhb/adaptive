package `fun`.adaptive.adat

import `fun`.adaptive.adat.descriptor.InstanceValidationResult
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.adat.store.AdatStore

/**
 * @property    property    The property this value
 */
class AdatContext<IT>(
    val id: Comparable<IT>?,
    val parent: AdatClass?,
    val property: AdatPropertyMetadata?,
    val store: AdatStore<*>?,
    var validationResult: InstanceValidationResult?
) {

    var localInfo: MutableList<Any>? = null

    val safeLocalInfo: MutableList<Any>
        get() = localInfo ?: mutableListOf<Any>().also { localInfo = it }

    class Flag(
        val propertyPath: Array<String>,
        val flagName: String
    )

    fun isTouched(path: Array<String>): Boolean {
        val info = localInfo ?: return false
        return info.indexOfFirst { it is Flag && it.propertyPath.contentEquals(path) && it.flagName == TOUCHED_FLAG } != - 1
    }

    fun touch(path: Array<String>) {
        if (isTouched(path)) return
        safeLocalInfo.add(Flag(path, TOUCHED_FLAG))
    }

    fun hasProblem(path: Array<String>): Boolean {
        val info = localInfo ?: return false
        return info.indexOfFirst { it is Flag && it.propertyPath.contentEquals(path) && it.flagName == HAS_PROBLEM } != - 1
    }

    fun addProblem(path: Array<String>) {
        if (hasProblem(path)) return
        safeLocalInfo.add(Flag(path, HAS_PROBLEM))
    }

    fun clearProblem(path: Array<String>) {
        val info = localInfo ?: return
        val index = info.indexOfFirst { it is Flag && it.propertyPath.contentEquals(path) && it.flagName == HAS_PROBLEM }
        if (index != - 1) info.removeAt(index)
    }

    fun apply(instance: AdatClass) {
        @Suppress("UNCHECKED_CAST")
        instance.adatContext = this as AdatContext<Any>?

        for (property in instance.getMetadata().properties) {
            if (! property.isAdatClass) continue

            // FIXME AdatContext.apply for adat class collections
            val sub = (instance.getValue(property.index) as? AdatClass)
            if (sub == null) continue

            AdatContext<Any>(null, instance, property, store, null).apply(sub)
        }
    }

    companion object {
        const val TOUCHED_FLAG = "touched"
        const val HAS_PROBLEM = "error"
    }
}


