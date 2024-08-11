/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package stuff

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adaptive
import `fun`.adaptive.foundation.testing.*
import `fun`.adaptive.foundation.query.*

@Adaptive
fun tf(
    vararg instructions: AdaptiveInstruction = emptyArray(),
) {
    tf2(*instructions)
    tf2(*instructions, TInst1)
}

@Adaptive
fun tf2(
    vararg instructions: AdaptiveInstruction,
) {

}

object TInst1 : AdaptiveInstruction
object TInst2 : AdaptiveInstruction

fun box(): String {

    adaptive(AdaptiveTestAdapter()) {
        tf()
    }

    // this test fails with a NullPointerException when null value of instructions is
    // not handled properly

    return "OK"
}