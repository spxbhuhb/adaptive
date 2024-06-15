/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package stuff

import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.foundation.instruction.*
import hu.simplexion.adaptive.foundation.query.*
import hu.simplexion.adaptive.foundation.fragment.*
import hu.simplexion.adaptive.foundation.testing.*

val gName = Name("5")
val aName = arrayOf(Name("6"), Name("7"))
object oInst : AdaptiveInstruction

fun fName() = arrayOf(Name("8"), Name("9"))

@Adaptive
fun ho(vararg instructions: AdaptiveInstruction, @Adaptive block: () -> Unit) {
    block()
}

fun box(): String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        ho {
            name("1")
            Name("2")
            arrayOf(name("3"), name("4"))
            gName
            aName
            oInst
            fName()
        }
    }

    val fragment = adapter.filter(true) { "ho" in it::class.simpleName!!.lowercase() }.single()
    val instructions = fragment.instructions

    for (i in 1..9) {
        if (Name("$i") !in instructions) return "Fail: Name(\"$i\") not in instructions"
    }

    if (oInst !in instructions) return "Fail: oInst !in instructions"

    return "OK"

}