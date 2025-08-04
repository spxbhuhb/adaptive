/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package stuff

import `fun`.adaptive.foundation.*
import `fun`.adaptive.foundation.instruction.*
import `fun`.adaptive.foundation.query.*
import `fun`.adaptive.foundation.fragment.*
import `fun`.adaptive.foundation.testing.*

@Adaptive
fun ho(inner : @Adaptive () -> Unit) : AdaptiveFragment {
    inner()
    return fragment()
}

fun box(): String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        ho {
            T0() .. name("a")
        }
    }

    val t0 = adapter.first(true) { name("a") in it.instructions }

    if (t0 !is AdaptiveT0) return "Fail: t0 !is T0"

    return "OK"

}