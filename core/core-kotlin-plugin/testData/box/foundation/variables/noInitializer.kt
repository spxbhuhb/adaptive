/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adaptive
import `fun`.adaptive.foundation.testing.*
import `fun`.adaptive.foundation.query.first

@Adaptive
fun Basic() {
    var a: Int
    S1 { a = it }
}

fun box(): String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        Basic()
    }

    val s1 = adapter.first<AdaptiveS1>(true)
    s1.s0(12)

    if (adapter.firstFragment.state[1] != 12) return "Fail: a != 12"

    return "OK"
}