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

    companion object {
        const val TOUCHED_FLAG = "touched"
    }
}


fun AdatClass.applyContext(context: AdatContext<Any>) {
    adatContext = context
    for (property in getMetadata().properties) {
        if (property.isAdatClass) {
            // FIXME AdatContext.apply for adat class collections
            (getValue(property.index) as? AdatClass)
                ?.applyContext(AdatContext<Any>(null, this, property, context.store, null))
        }
    }
}

fun AdatClass.absolutePath(): List<String> {
    val context: AdatContext<*> = adatContext ?: return emptyList()
    val parent = context.parent ?: return emptyList()
    val property = context.property ?: return emptyList()
    return parent.absolutePath() + property.name
}