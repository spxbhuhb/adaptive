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
fun ho(vararg instructions: AdaptiveInstruction, block: @Adaptive () -> Unit) {
    block()
}

fun box(): String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {

        val a = true

        ho {
            if (a) name("a") else name("b")
        }

        ho {
            if (! a) name("a") else name("b")
        }

    }

    val fragments = adapter.filter(true) { "ho" in it::class.simpleName !!.lowercase() }

    if (fragments.size != 2) return "Fail: fragments.size != 2"

    if (fragments[0].instructions.size != 1) return "Fail: fragments[0].instructions.size != 1"
    if (name("a") !in fragments[0].instructions) return "Fail: name(\"a\") !in fragments[0].instructions"

    if (fragments[1].instructions.size != 1) return "Fail: fragments[1].instructions.size != 1"
    if (name("b") !in fragments[1].instructions) return "Fail: name(\"b\") !in fragments[1].instructions"

    return "OK"

}