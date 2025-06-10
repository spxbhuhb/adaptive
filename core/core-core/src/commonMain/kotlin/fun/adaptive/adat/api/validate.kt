package `fun`.adaptive.adat.api

import `fun`.adaptive.adat.AdatClass

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