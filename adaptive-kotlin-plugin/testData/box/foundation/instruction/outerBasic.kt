/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package stuff

import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.foundation.instruction.*
import hu.simplexion.adaptive.foundation.query.*
import hu.simplexion.adaptive.foundation.fragment.*
import hu.simplexion.adaptive.foundation.testing.*

@Adaptive
fun testFragment(vararg instructions: AdaptiveInstruction, @Adaptive block: () -> Unit): AdaptiveFragment {
    block()
    return fragment()
}

val names = arrayOf(name("4"), name("5"))

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

    if (fragments[0].instructions.size != 1) return "Fail: fragments[0].instructions.size != 1"
    if (name("stuff - 1") !in fragments[0].instructions) return "Fail: name(\"stuff - 1\") !in fragments[0].instructions"

    if (fragments[1].instructions.size != 3) return "Fail: fragments[1].instructions.size != 3"
    if (name("stuff - normal") != fragments[1].instructions[0]) return "Fail: name(\"stuff - normal\") != fragments[1].instructions[0]"
    if (name("stuff - inner") != fragments[1].instructions[1]) return "Fail: name(\"stuff - inner\") != fragments[1].instructions[1]"
    if (name("stuff - outer") != fragments[1].instructions[2]) return "Fail: name(\"stuff - outer\") != fragments[1].instructions[2]"

    if (fragments[2].instructions.size != 2) return "Fail: fragments[2].instructions.size != 2"
    if (name("1") != fragments[2].instructions[0]) return "Fail: name(\"1\") != fragments[2].instructions[0]"
    if (name("2") != fragments[2].instructions[1]) return "Fail: name(\"2\") != fragments[2].instructions[1]"

    if (fragments[3].instructions.size != 2) return "Fail: fragments[3].instructions.size != 2"
    if (name("4") != fragments[3].instructions[0]) return "Fail: name(\"4\") != fragments[3].instructions[0]"
    if (name("5") != fragments[3].instructions[1]) return "Fail: name(\"5\") != fragments[3].instructions[1]"

    return "OK"

}