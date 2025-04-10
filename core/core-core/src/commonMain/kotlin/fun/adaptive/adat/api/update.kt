package `fun`.adaptive.adat.api

import `fun`.adaptive.adat.AdatClass
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0

fun <A : AdatClass> A.update(newValue: A) {
    store().update(this, newValue)
}

fun <A : AdatClass, V> A.update(property: KProperty0<V>, value: V) {
    store().update(this, arrayOf(property.name), value)
}

fun <A : AdatClass> A.update(vararg changes: Pair<KProperty<*>, *>): A =
    store().update(this, changes)

fun <A : AdatClass> A.update(changes: Collection<Pair<KProperty<*>, *>>): A =
    this.update(*(changes.toTypedArray()))