/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.foundation.*
import `fun`.adaptive.foundation.testing.*

var a = 0

fun box() : String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        val b = 12
        S1 { a = it + b }
    }

    val s1 = adapter.rootFragment.children.first() as AdaptiveS1

    s1.s0.invoke(13)

    if (a != 25) return "Fail: a != 25"

    return "OK"
}