package `fun`.adaptive.resource.resolve

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.resource.StringResourceKey
import `fun`.adaptive.runtime.AbstractApplication
import `fun`.adaptive.runtime.AbstractWorkspace

fun AbstractWorkspace.resolveString(key : StringResourceKey) : String =
    application?.resolveString(key) ?: key

fun AdaptiveFragment.resolveString(key : StringResourceKey) : String =
    adapter.application?.resolveString(key) ?: key

fun AbstractApplication<*,*>.resolveString(key : StringResourceKey) : String? {
    for (store in stringStores) {
        store.getOrNull(key)?.let { return it }
    }
    return null
}