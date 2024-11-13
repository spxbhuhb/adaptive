package `fun`.adaptive.adat.api

import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.foundation.replacedByPlugin

fun <A> adatCompanionOf(): AdatCompanion<A> {
    replacedByPlugin("adatCompanionOf is replaced by the plugin")
}