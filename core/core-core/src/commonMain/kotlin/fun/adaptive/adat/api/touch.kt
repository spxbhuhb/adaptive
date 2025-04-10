package `fun`.adaptive.adat.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.store.AdatStore

fun <A : AdatClass> A.touchAndValidate(): Boolean {
    val context = this.adatContext ?: return false
    context.touchAll()

    // to update the UI
    @Suppress("UNCHECKED_CAST")
    (context.store as? AdatStore<A>)?.update(this)

    return validate().isValid
}