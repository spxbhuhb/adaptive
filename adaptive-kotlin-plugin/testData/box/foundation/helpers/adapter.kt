/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.foundation.*
import `fun`.adaptive.foundation.testing.*

var actual : AdaptiveAdapter? = null

fun box() : String {

    val expected = AdaptiveTestAdapter()

    adaptive(expected) {
        actual = adapter()
    }

    return if (expected == actual) "OK" else "Fail: returned adapter is not the same: $expected $actual"
}