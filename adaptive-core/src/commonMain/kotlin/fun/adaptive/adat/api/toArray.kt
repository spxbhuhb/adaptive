package `fun`.adaptive.adat.api

import `fun`.adaptive.adat.AdatClass

fun AdatClass.toArray(): Array<Any?> {
    val properties = getMetadata().properties
    return Array(properties.size) { i -> getValue(i) }
}