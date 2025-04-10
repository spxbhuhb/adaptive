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
fun outer(vararg instructions: AdaptiveInstruction)  : AdaptiveFragment {
    inner(name("outer"), instructions())
    return fragment()
}


@Adaptive
fun inner(vararg instructions: AdaptiveInstruction) : AdaptiveFragment {
    return fragment()
}

fun box(): String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        outer() .. name("box")
    }

    adapter.checkInstructions("inner", 0, name("box"), name("outer"))

    adapter.changeState("outer", 0, 0) { instructionsOf(name("changed")) }

    adapter.checkInstructions("inner", 0, name("changed"), name("outer"))

    return adapter.checkResults

}