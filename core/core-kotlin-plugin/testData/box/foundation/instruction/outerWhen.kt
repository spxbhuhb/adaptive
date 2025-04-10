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
fun outer(vararg instructions: AdaptiveInstruction, @Adaptive block: () -> Unit) {
    block()
}


@Adaptive
fun ho2(vararg instructions: AdaptiveInstruction) : AdaptiveFragment {
    T0()
    return fragment()
}

fun box(): String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        outer {
            if (true) {
                ho2() .. name("1")
            } else {
                ho2() .. name("2")
            }
        }
    }

    adapter.checkInstructions("ho2", 0, name("1"))

    return adapter.checkResults

}