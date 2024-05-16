/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.base.success

import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.foundation.testing.*

var actual : AdaptiveFragment<*>? = null

fun box() : String {

    val testAdapter = AdaptiveTestAdapter()

    adaptive(testAdapter) {
        actual = fragment()
    }

    return if (testAdapter.rootFragment == actual) "OK" else "Fail: returned adapter is not the same: ${testAdapter.rootFragment} $actual"

}