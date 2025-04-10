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

fun box(): String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        ho {

        }

        ho(name("1")) {

        }

        ho {
            name("2")
        }

        ho(name("1")) {
            name("2")
        }

        ho {
            T0()
        }

        ho(name("1")) {
            T0()
        }

        ho(name("1")) {
            name("2")
            T0()
        }
    }

    val fragments = adapter.filter(true) { "ho" in it::class.simpleName!!.lowercase() }

    if (fragments.size != 7) return "Fail: fragments.size != 7"

    if (fragments[0].instructions.isNotEmpty()) return "Fail: fragments[0].instructions.isNotEmpty()"

    if (fragments[1].instructions.size != 1) return "Fail: fragments[1].instructions.size != 1"
    if (name("1") !in fragments[1].instructions) return "Fail: name(\"1\") !in fragments[1].instructions"

    if (fragments[2].instructions.size != 1) return "Fail: fragments[2].instructions.size != 1"
    if (name("2") !in fragments[2].instructions) return "Fail: name(\"2\") !in fragments[2].instructions"

    if (fragments[3].instructions.size != 2) return "Fail: fragments[3].instructions.size != 2"
    if (name("1") !in fragments[3].instructions) return "Fail: name(\"1\") !in fragments[3].instructions"
    if (name("2") !in fragments[3].instructions) return "Fail: name(\"2\") !in fragments[3].instructions"

    if (fragments[4].instructions.size != 0) return "Fail: fragments[4].instructions.size != 0"

    if (fragments[5].instructions.size != 1) return "Fail: fragments[5].instructions.size != 1"
    if (name("1") !in fragments[5].instructions) return "Fail: name(\"1\") !in fragments[5].instructions"

    if (fragments[6].instructions.size != 2) return "Fail: fragments[6].instructions.size != 2"
    if (name("1") !in fragments[6].instructions) return "Fail: name(\"1\") !in fragments[6].instructions"
    if (name("2") !in fragments[6].instructions) return "Fail: name(\"2\") !in fragments[6].instructions"

    return "OK"

}