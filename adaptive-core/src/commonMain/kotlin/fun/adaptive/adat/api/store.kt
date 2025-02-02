package `fun`.adaptive.adat.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.store.AdatStore
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0

fun <A : AdatClass> A.store(): AdatStore<A> {
    val store = checkNotNull(adatContext?.store) { "no store for $this" }
    @Suppress("UNCHECKED_CAST")
    return store as AdatStore<A>
}

fun <A : AdatClass> A.storeOrNull(): AdatStore<A>? {
    val store = adatContext?.store ?: return null
    @Suppress("UNCHECKED_CAST")
    return store as AdatStore<A>
}