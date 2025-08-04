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
fun outer(vararg instructions: AdaptiveInstruction, block: @Adaptive () -> Unit) {
    block()
}


@Adaptive
fun ho2(vararg instructions: AdaptiveInstruction, block: @Adaptive () -> Unit) {
    block()
}

fun box(): String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        outer {
            ho2 {
                name("1")
            }
        }
    }

    val fragment = adapter.filter(true) { "ho" in it::class.simpleName!!.lowercase() }.single()
    val instructions = fragment.instructions

    return "OK"

}