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
fun tf(vararg instructions: AdaptiveInstruction) {
    T0()
}

object TInst1 : AdaptiveInstruction
object TInst2 : AdaptiveInstruction

fun box(): String {

    adaptive(AdaptiveTestAdapter()) {

        tf(TInst1, TInst2)
        tf()

    }.also { adapter ->

        val f = adapter.firstWith<TInst1>()

        if (! f.instructions.contentEquals(arrayOf(TInst1, TInst2))) return "Fail:content"

    }

    return "OK"
}