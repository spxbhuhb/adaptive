package `fun`.adaptive.adat.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatContext

fun AdatClass.absolutePath(): List<String> {
    val context: AdatContext<*> = adatContext ?: return emptyList()
    val parent = context.parent ?: return emptyList()
    val property = context.property ?: return emptyList()
    return parent.absolutePath() + property.name
}