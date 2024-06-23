/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package stuff

import hu.simplexion.adaptive.foundation.instruction.*
import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.foundation.testing.*
import hu.simplexion.adaptive.foundation.fragment.*
import hu.simplexion.adaptive.foundation.query.*

@Adaptive
fun text(content : String, vararg instructions : AdaptiveInstruction) {

}

class Append(
    @AdaptiveDetach val buildFun: (handler: DetachHandler) -> Unit
) : DetachHandler, AdaptiveInstruction {

    // this results in a call to `detach`
    override fun execute() {
        buildFun(this)
    }

    override fun detach(origin: AdaptiveFragment, detachIndex: Int) {
        origin.genBuild(origin, detachIndex)?.also {
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
    @AdaptiveDetach slotEntry: (handler: DetachHandler) -> Unit
) = Append(slotEntry)

fun box(): String {

    val adapter = AdaptiveTestAdapter()
    //adapter.printTrace = true

    adaptive(adapter) {
        text("Label", append { T1(12) })
        text("Label", append { T1(23) })
    }

    adapter.collect<Append>().forEach { it.execute() }

    return adapter.assert(listOf(
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveSequence", 3, "before-Create", ""),
        TraceEvent("AdaptiveSequence", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveSequence", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, 3]"),
        TraceEvent("AdaptiveSequence", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, 3]"),
        TraceEvent("AdaptiveText", 4, "before-Create", ""),
        TraceEvent("AdaptiveText", 4, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveText", 4, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [Label, [Detach]]"),
        TraceEvent("AdaptiveText", 4, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [Label, [Detach]]"),
        TraceEvent("AdaptiveText", 4, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [Label, [Detach]]"),
        TraceEvent("AdaptiveSequence", 5, "before-Create", ""),
        TraceEvent("AdaptiveSequence", 5, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveSequence", 5, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveSequence", 5, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveSequence", 5, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveSequence", 5, "after-Create", ""),
        TraceEvent("AdaptiveText", 4, "after-Create", ""),
        TraceEvent("AdaptiveText", 6, "before-Create", ""),
        TraceEvent("AdaptiveText", 6, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveText", 6, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [Label, [Detach]]"),
        TraceEvent("AdaptiveText", 6, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [Label, [Detach]]"),
        TraceEvent("AdaptiveText", 6, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [Label, [Detach]]"),
        TraceEvent("AdaptiveSequence", 7, "before-Create", ""),
        TraceEvent("AdaptiveSequence", 7, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveSequence", 7, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveSequence", 7, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveSequence", 7, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveSequence", 7, "after-Create", ""),
        TraceEvent("AdaptiveText", 6, "after-Create", ""),
        TraceEvent("AdaptiveSequence", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [1, 3]"),
        TraceEvent("AdaptiveSequence", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", ""),
        TraceEvent("AdaptiveSequence", 3, "before-Mount", ""),
        TraceEvent("AdaptiveText", 4, "before-Mount", ""),
        TraceEvent("AdaptiveSequence", 5, "before-Mount", ""),
        TraceEvent("AdaptiveSequence", 5, "after-Mount", ""),
        TraceEvent("AdaptiveText", 4, "after-Mount", ""),
        TraceEvent("AdaptiveText", 6, "before-Mount", ""),
        TraceEvent("AdaptiveSequence", 7, "before-Mount", ""),
        TraceEvent("AdaptiveSequence", 7, "after-Mount", ""),
        TraceEvent("AdaptiveText", 6, "after-Mount", ""),
        TraceEvent("AdaptiveSequence", 3, "after-Mount", ""),
        TraceEvent("<root>", 2, "after-Mount", ""),
        TraceEvent("AdaptiveT1", 8, "before-Create", ""),
        TraceEvent("AdaptiveT1", 8, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveT1", 8, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
        TraceEvent("AdaptiveT1", 8, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
        TraceEvent("AdaptiveT1", 8, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
        TraceEvent("AdaptiveT1", 8, "after-Create", ""),
        TraceEvent("AdaptiveT1", 8, "before-Create", ""),
        TraceEvent("AdaptiveT1", 8, "before-Patch-External", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
        TraceEvent("AdaptiveT1", 8, "after-Patch-External", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
        TraceEvent("AdaptiveT1", 8, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
        TraceEvent("AdaptiveT1", 8, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
        TraceEvent("AdaptiveT1", 8, "after-Create", ""),
        TraceEvent("AdaptiveT1", 8, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 8, "after-Mount", ""),
        TraceEvent("AdaptiveT1", 9, "before-Create", ""),
        TraceEvent("AdaptiveT1", 9, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveT1", 9, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [23]"),
        TraceEvent("AdaptiveT1", 9, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [23]"),
        TraceEvent("AdaptiveT1", 9, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [23]"),
        TraceEvent("AdaptiveT1", 9, "after-Create", ""),
        TraceEvent("AdaptiveT1", 9, "before-Create", ""),
        TraceEvent("AdaptiveT1", 9, "before-Patch-External", "createMask: 0x00000000 thisMask: 0x00000000 state: [23]"),
        TraceEvent("AdaptiveT1", 9, "after-Patch-External", "createMask: 0x00000000 thisMask: 0x00000000 state: [23]"),
        TraceEvent("AdaptiveT1", 9, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [23]"),
        TraceEvent("AdaptiveT1", 9, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [23]"),
        TraceEvent("AdaptiveT1", 9, "after-Create", ""),
        TraceEvent("AdaptiveT1", 9, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 9, "after-Mount", "")
    ))
}