package `fun`.adaptive.adat.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.store.AdatStore
import kotlin.reflect.KProperty0

fun <A : AdatClass> A.store(): AdatStore<A> {
    val store = checkNotNull(adatContext?.store) { "no store for $this" }
    @Suppress("UNCHECKED_CAST")
    return store as AdatStore<A>
}

fun <A : AdatClass> A.storeOrNull(): AdatStore<A>? {
    @Suppress("UNCHECKED_CAST")
    return adatContext?.store as AdatStore<A>
}

fun <A : AdatClass> A.update(newValue: A) {
    store().update(this, newValue)
}

fun <A : AdatClass, V> A.update(property : KProperty0<V>, value : V) {
    store().update(this, arrayOf(property.name), value)
}