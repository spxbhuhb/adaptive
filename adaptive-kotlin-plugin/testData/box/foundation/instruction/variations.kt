/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package stuff

import `fun`.adaptive.foundation.instruction.*
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adaptive
import `fun`.adaptive.foundation.testing.*

@Adaptive
fun itN() {
    T0()
}

// FIXME checker for invalid `instructions` argument

//@Adaptive
//fun itN1(instructions: Int) {
//    T0()
//}
//
//@Adaptive
//fun itN2(instructions: Array<Int>) {
//    T0()
//}

@Adaptive
fun it0(vararg instructions: AdaptiveInstruction) {
    T0()
}

@Adaptive
fun it1(i: Int, vararg instructions: AdaptiveInstruction) {
    T0()
}

@Adaptive
fun it2(vararg instructions: AdaptiveInstruction, i: Int) {
    T0()
}

object TInst1 : AdaptiveInstruction
object TInst2 : AdaptiveInstruction

fun box(): String {

    adaptive(AdaptiveTestAdapter()) {

        itN()

        it0() // 1

        it1(14) // 2
        it1(15, TInst1) // 3

        it2(i = 16) // 4
        it2(TInst1, i = 17) // 5

    }.also {
        val children = it.firstFragment.children
        if (children.size != 6) return "Fail: children.size"

        println(children[3].instructions)

        if (children[0].instructions.isNotEmpty()) return "Fail: children[0]"
        if (children[1].instructions.isNotEmpty()) return "Fail: children[1]"
        if (children[2].instructions.isNotEmpty()) return "Fail: children[2]"
        if (children[3].instructions != instructionsOf(TInst1)) return "Fail: children[3].instructions"
        if (children[4].instructions.isNotEmpty()) return "Fail: children[4].instructions"
        if (children[5].instructions != instructionsOf(TInst1)) return "Fail: children[5].instructions"

    }

    return "OK"
}