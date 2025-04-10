package `fun`.adaptive.adat.api

import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.foundation.replacedByPlugin

inline fun <reified A> adatCompanionOf(): AdatCompanion<A> {
    replacedByPlugin("adatCompanionOf is replaced by the plugin")
}