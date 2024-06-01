/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package stuff

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.adaptive
import hu.simplexion.adaptive.foundation.testing.*
import hu.simplexion.adaptive.foundation.select.*

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