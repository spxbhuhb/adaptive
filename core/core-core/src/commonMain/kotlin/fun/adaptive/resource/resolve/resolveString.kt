package `fun`.adaptive.resource.resolve

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.resource.StringResourceKey

fun AdaptiveFragment.resolveString(key : StringResourceKey) : String {
    val app = adapter.application ?: return key

    for (store in app.stringStores) {
        val label = store.getOrNull(key) ?: continue
        return label
    }

    return key
}