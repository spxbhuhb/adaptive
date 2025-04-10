package `fun`.adaptive.adat.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.descriptor.InstanceValidationResult

fun <A : AdatClass> A.validate(): InstanceValidationResult {

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
fun AdatClass.isNotValid(): Boolean =
    ! isValid()

/**
 * Check if the Adat instance is valid.
 *
 * @return  true if the instance is valid, false otherwise
 */
fun AdatClass.isValid(): Boolean {
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
fun AdatClass.isValid(name: String): Boolean =
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
fun AdatClass.isValid(path: Array<String>): Boolean {
    check(path.size == 1) { "only simple paths are supported" }
    val result = adatContext?.validationResult?.failedConstraints?.filter { it.property?.name == path[0] }
    return (result == null || result.isEmpty())
}

/**
 * Check if the Adat instance context has a problem. This is useful for editors when it is
 * possible that an edited field has a problem (such as wrong value format). In that case
 * editors add the problem to `localInfo` of the adat context.
 *
 * @return  true if the instance is valid, false otherwise
 */
fun AdatClass.hasProblem(): Boolean =
    adatContext?.hasProblem() == true

/**
 * Check if the property selected by [path] has any problems.
 *
 * This call is meaningful only when the adat class has a context.
 *
 * **TODO** Deep path support (AdatClass.hasProblem)
 *
 * @return  true if the data has a problem, false otherwise
 */
fun AdatClass.hasProblem(path: Array<String>): Boolean {
    return adatContext?.hasProblem(path) == true
}