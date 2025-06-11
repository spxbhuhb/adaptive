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
fun outer(b: Boolean): AdaptiveFragment {
    inner(name("outer")) .. if (b) instructions() .. name("b") else instructions()
    return fragment()
}

@Adaptive
fun inner(vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    return fragment()
}

fun box(): String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        outer(true) // when no instructions is provided, the instructions() call in `outer` should not fail
    }

    adapter.checkInstructions("inner", 0, name("outer"), name("b"))

    return adapter.checkResults

}