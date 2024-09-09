package `fun`.adaptive.adat.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatContext
import `fun`.adaptive.adat.descriptor.InstanceValidationResult
import `fun`.adaptive.adat.descriptor.PropertiesBuilder
import `fun`.adaptive.foundation.replacedByPlugin

/**
 * Provide builder context for property descriptor builder.
 * See Adat documentation for more details.
 */
fun AdatClass.properties(buildFun: PropertiesBuilder.() -> Unit) {
    replacedByPlugin("", buildFun)
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
fun AdatClass.isTouched(path: Array<String>): Boolean =
    adatContext?.isTouched(path) == true

/**
 * Validate the Adat instance and store the result in the context.
 * Create a new context if there is no context.
 */
fun AdatClass.validateForContext(): InstanceValidationResult {

    val result = validate()

    val context = adatContext
    if (context == null) {
        adatContext = AdatContext(null, null, null, null, result)
    } else {
        context.validationResult = result
    }

    return result

}