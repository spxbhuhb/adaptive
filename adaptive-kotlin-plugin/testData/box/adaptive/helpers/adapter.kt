/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.adaptive.base.*
import hu.simplexion.adaptive.base.testing.*

var actual : AdaptiveAdapter<*>? = null

fun box() : String {

    val expected = AdaptiveTestAdapter()

    adaptive(expected) {
        actual = adapter()
    }

    return if (expected == actual) "OK" else "Fail: returned adapter is not the same: $expected $actual"
}