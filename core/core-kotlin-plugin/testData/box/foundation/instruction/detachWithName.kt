/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package stuff

import `fun`.adaptive.foundation.*
import `fun`.adaptive.foundation.fragment.*
import `fun`.adaptive.foundation.instruction.*
import `fun`.adaptive.foundation.query.*
import `fun`.adaptive.foundation.testing.*

@Adaptive
fun text(content: String, vararg instructions: AdaptiveInstruction) {

}

class Append(
    @DetachName val name: String?,
    @AdaptiveDetach val buildFun: (handler: DetachHandler) -> Unit
) : DetachHandler, AdaptiveInstruction {

    // this results in a call to `detach`
    override fun execute() {
        buildFun(this)
    }

    override fun detach(origin: AdaptiveFragment, detachIndex: Int) {
        origin.genBuild(origin, detachIndex, AdaptiveFragment.DETACHED_MASK)?.also {
            origin.children += it
            it.create()
            it.mount()
        }
    }

    override fun toString(): String {
        return "Detach"
    }
}

fun append(
    @DetachName name: String? = null,
    @AdaptiveDetach buildFun: (handler: DetachHandler) -> Unit
) = Append(name, buildFun)

fun box(): String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        text("Label", append { T1(12) })
    }

    val inst = adapter.collect<Append>().single()

    if (inst.name != "fun.adaptive.foundation.testing.AdaptiveT1") return "Fail: inst.name == T1 (${inst.name})"

    return "OK"
}