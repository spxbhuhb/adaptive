package `fun`.adaptive.adat

import `fun`.adaptive.adat.descriptor.result.InstanceValidationResult
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.adat.store.AdatStore

/**
 * @property    property    The property this value
 */
class AdatContext<IT>(
    val id : Comparable<IT>?,
    val parent: AdatClass<*>?,
    val property: AdatPropertyMetadata?,
    val store: AdatStore?,
    var validationResult: InstanceValidationResult?
)

fun AdatClass<*>.applyContext(context: AdatContext<Any>) {
    adatContext = context
    for (property in getMetadata().properties) {
        if (property.isAdatClass) {
            // FIXME AdatContext.apply for adat class collections
            (getValue(property.index) as? AdatClass<*>)
                ?.applyContext(AdatContext<Any>(null, this, property, context.store, null))
        }
    }
}

fun AdatClass<*>.absolutePath(): List<String> {
    val context : AdatContext<*> = adatContext ?: return emptyList()
    val parent = context.parent ?: return emptyList()
    val property = context.property ?: return emptyList()
    return parent.absolutePath() + property.name
}