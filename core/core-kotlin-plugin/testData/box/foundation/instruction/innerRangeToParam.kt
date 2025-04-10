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
fun ho(vararg instructions: AdaptiveInstruction, @Adaptive block: () -> Unit) {
    block()
}

@Adaptive
fun t1(n: Name) {
    ho {
        n .. name("1")
    }
    ho {
        name("2") .. n
    }
}

fun box(): String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        t1(name("3"))
    }

    adapter.checkInstructions("ho", 0, name("3"), name("1"))
    adapter.checkInstructions("ho", 1, name("2"), name("3"))

    return adapter.checkResults

}