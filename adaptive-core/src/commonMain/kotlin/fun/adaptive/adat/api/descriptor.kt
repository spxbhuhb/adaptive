package `fun`.adaptive.adat.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.descriptor.PropertiesBuilder

/**
 * Provide builder context for property descriptor builder.
 * See Adat documentation for more details.
 */
fun AdatClass<*>.properties(buildFun : PropertiesBuilder.() -> Unit) {

}

/**
 * Check if the property selected by [path] has any validation errors.
 *
 * This call is meaningful only after the `validation` function of the
 * Adat instance is called. Built-in producers such as `copyStore`, `autoInstance`
 * and `autoList` call `validate` automatically when the data changes.
 *
 * **TODO** Deep path support (AdatClass<*>.isValid)
 *
 * @return  true if the data is valid, false otherwise
 */
fun AdatClass<*>.isValid(path : Array<String>) : Boolean {
    check(path.size == 1) { "only simple paths are supported" }
    val result = adatContext?.validationResult?.failedConstraints?.filter { it.property?.name == path[0] }
    return (result == null || result.isEmpty())
}

/**
 * Check if the property has been touched by the user on the UI. Before a field
 * is touched the UI typically should **not** display any error messages to
 * the user as this would result in forms full of errors when editing starts.
 *
 * This information is stored in the `AdatContext.localInfo`, UI editor fragments
 * typically change it on first user interaction and on submit attempt.
 *
 * @return  true if the field is touched, false otherwise
 */
fun AdatClass<*>.isTouched(path : Array<String>) : Boolean =
    adatContext?.isTouched(path) == true
