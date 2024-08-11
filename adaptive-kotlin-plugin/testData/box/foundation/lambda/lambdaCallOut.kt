/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.foundation.*
import `fun`.adaptive.foundation.testing.*

fun box(): String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        val b = 12
        S1R { mock(b) }
    }

    val s1r = adapter.rootFragment.children.first() as AdaptiveS1R

    val a = s1r.s0.invoke(13)

    if (a != 13) return "Fail: a != 13 ($a)"

    return "OK"
}

fun mock(i: Int): Int {
    return i + 1
}