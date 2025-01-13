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
fun testFragment(vararg instructions: AdaptiveInstruction, @Adaptive block: () -> Unit): AdaptiveFragment {
    block()
    return fragment()
}

val names = instructionsOf(name("4"), name("5"))

fun box(): String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        testFragment { } .. name("stuff - 1")

        testFragment(
            name("stuff - normal")
        ) {
            name("stuff - inner")
        } .. name("stuff - outer")

        testFragment { } .. name("1") .. name("2")

        testFragment { } .. names
    }

    val fragments = adapter.filter(true) { it.instructions.any { it is Name } }

    val fi0 = fragments[0].instructions.toMutableList()
    if (fi0.size != 1) return "Fail: fragments[0].instructions.size != 1"
    if (name("stuff - 1") !in fi0) return "Fail: name(\"stuff - 1\") !in fragments[0].instructions"

    val fi1 = fragments[1].instructions.toMutableList()
    if (fi1.size != 3) return "Fail: fragments[1].instructions.size != 3"
    if (name("stuff - normal") != fi1[0]) return "Fail: name(\"stuff - normal\") != fragments[1].instructions[0]"
    if (name("stuff - inner") != fi1[1]) return "Fail: name(\"stuff - inner\") != fragments[1].instructions[1]"
    if (name("stuff - outer") != fi1[2]) return "Fail: name(\"stuff - outer\") != fragments[1].instructions[2]"

    val fi2 = fragments[2].instructions.toMutableList()
    if (fi2.size != 2) return "Fail: fragments[2].instructions.size != 2"
    if (name("1") != fi2[0]) return "Fail: name(\"1\") != fragments[2].instructions[0]"
    if (name("2") != fi2[1]) return "Fail: name(\"2\") != fragments[2].instructions[1]"

    val fi3 = fragments[3].instructions.toMutableList()
    if (fi3.size != 2) return "Fail: fragments[3].instructions.size != 2"
    if (name("4") != fi3[0]) return "Fail: name(\"4\") != fragments[3].instructions[0]"
    if (name("5") != fi3[1]) return "Fail: name(\"5\") != fragments[3].instructions[1]"

    return "OK"

}
