/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package stuff

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.adaptive
import hu.simplexion.adaptive.foundation.testing.*

@Adaptive
fun itN() {
    T0()
}

@Adaptive
fun itN1(instructions: Int) {
    T0()
}

@Adaptive
fun itN2(instructions: Array<Int>) {
    T0()
}

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
        itN1(12)
        itN2(arrayOf(13))

        it0() // 3

        it1(14) // 4
        it1(15, TInst1) // 5

        it2(i = 16) // 6
        it2(TInst1, i = 17) // 7

    }.also {
        val children = it.rootFragment.children.first().children
        if (children.size != 8) return "Fail: children.size"

        if (children[0].instructionIndex != - 1) return "Fail: children[0]"
        if (children[1].instructionIndex != - 1) return "Fail: children[1]"
        if (children[2].instructionIndex != - 1) return "Fail: children[2]"

        if (children[3].instructionIndex != 0) return "Fail: children[3]"
        if (children[3].instructions.isNotEmpty()) return "Fail: children[3].instructions"

        if (children[4].instructionIndex != 1) return "Fail: children[4]"
        if (children[4].instructions.isNotEmpty()) return "Fail: children[4].instructions"

        if (children[5].instructionIndex != 1) return "Fail: children[5]"
        if (! children[5].instructions.contentEquals(arrayOf(TInst1))) return "Fail: children[5].instructions"

        if (children[6].instructionIndex != 0) return "Fail: children[6]"
        if (children[6].instructions.isNotEmpty()) return "Fail: children[6].instructions"

        if (children[7].instructionIndex != 0) return "Fail: children[7]"
        if (! children[7].instructions.contentEquals(arrayOf(TInst1))) return "Fail: children[7].instructions"

    }

    return "OK"
}