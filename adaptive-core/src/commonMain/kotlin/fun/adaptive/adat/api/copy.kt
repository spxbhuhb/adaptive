package `fun`.adaptive.adat.api

import `fun`.adaptive.adat.AdatClass

fun <A : AdatClass> A.copy(): A {
    val properties = getMetadata().properties
    val values = arrayOfNulls<Any?>(properties.size)

    getMetadata().properties.forEach { prop ->
        values[prop.index] = getValue(prop.index)
    }

    @Suppress("UNCHECKED_CAST")
    return adatCompanion.newInstance(values) as A
}