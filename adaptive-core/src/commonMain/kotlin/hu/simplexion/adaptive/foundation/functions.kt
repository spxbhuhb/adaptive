/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation

import kotlin.js.JsName

fun adapter(): AdaptiveAdapter {
    replacedByPlugin("gets the adapter of the fragment")
}

@JsName("getFragment") // to avoid compiler warning with the fragment package
fun fragment(): AdaptiveFragment {
    replacedByPlugin("gets the fragment")
}

fun <T : AdaptiveTransformInterface> thisState(): T {
    replacedByPlugin("gets the fragment as a transform interface")
}