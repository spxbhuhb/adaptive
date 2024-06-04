/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package stuff

import hu.simplexion.adaptive.foundation.instruction.*
import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.foundation.testing.*
import hu.simplexion.adaptive.foundation.structural.*
import hu.simplexion.adaptive.foundation.query.*

@Adaptive
fun text(content : String, vararg instructions : AdaptiveInstruction) {

}

class Replace(
    @AdaptiveDetach val slotEntry: (handler: DetachHandler) -> Unit
) : DetachHandler, AdaptiveInstruction {

    override fun execute() {
        slotEntry(this)
    }

    override fun detach(origin: AdaptiveFragment, detachIndex: Int) {
        origin.single<AdaptiveSlot>(true).setContent(origin, detachIndex)
    }

    override fun toString(): String {
        return "Replace"
    }
}

fun replace(
    @AdaptiveDetach slotEntry: (handler: DetachHandler) -> Unit
) = Replace(slotEntry)

fun box(): String {

    val adapter = AdaptiveTestAdapter()
    //adapter.printTrace = true

    adaptive(adapter) {
        slot("content") { T0() }
        text("Label", Replace { T1(12) })
        text("Label", replace { T1(23) })
    }

    adapter.collect<Replace>().forEach { it.execute() }

    return adapter.assert(listOf(
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveSequence", 3, "before-Create", ""),
        TraceEvent("AdaptiveSequence", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveSequence", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, 3, 5]"),
        TraceEvent("AdaptiveSequence", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, 3, 5]"),
        TraceEvent("AdaptiveSlot", 4, "before-Create", ""),
        TraceEvent("AdaptiveSlot", 4, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveSlot", 4, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [content, BoundFragmentFactory(2,2)]"),
        TraceEvent("AdaptiveSlot", 4, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [content, BoundFragmentFactory(2,2)]"),
        TraceEvent("AdaptiveSlot", 4, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [content, BoundFragmentFactory(2,2)]"),
        TraceEvent("AdaptiveT0", 5, "before-Create", ""),
        TraceEvent("AdaptiveT0", 5, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveT0", 5, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveT0", 5, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveT0", 5, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveT0", 5, "after-Create", ""),
        TraceEvent("AdaptiveSlot", 4, "after-Create", ""),
        TraceEvent("AdaptiveText", 6, "before-Create", ""),
        TraceEvent("AdaptiveText", 6, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveText", 6, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [Label, [Replace]]"),
        TraceEvent("AdaptiveText", 6, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [Label, [Replace]]"),
        TraceEvent("AdaptiveText", 6, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [Label, [Replace]]"),
        TraceEvent("AdaptiveSequence", 7, "before-Create", ""),
        TraceEvent("AdaptiveSequence", 7, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveSequence", 7, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveSequence", 7, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveSequence", 7, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveSequence", 7, "after-Create", ""),
        TraceEvent("AdaptiveText", 6, "after-Create", ""),
        TraceEvent("AdaptiveText", 8, "before-Create", ""),
        TraceEvent("AdaptiveText", 8, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveText", 8, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [Label, [Replace]]"),
        TraceEvent("AdaptiveText", 8, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [Label, [Replace]]"),
        TraceEvent("AdaptiveText", 8, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [Label, [Replace]]"),
        TraceEvent("AdaptiveSequence", 9, "before-Create", ""),
        TraceEvent("AdaptiveSequence", 9, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveSequence", 9, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveSequence", 9, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveSequence", 9, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveSequence", 9, "after-Create", ""),
        TraceEvent("AdaptiveText", 8, "after-Create", ""),
        TraceEvent("AdaptiveSequence", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [1, 3, 5]"),
        TraceEvent("AdaptiveSequence", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", ""),
        TraceEvent("AdaptiveSequence", 3, "before-Mount", ""),
        TraceEvent("AdaptiveSlot", 4, "before-Mount", ""),
        TraceEvent("AdaptiveT0", 5, "before-Mount", ""),
        TraceEvent("AdaptiveT0", 5, "after-Mount", ""),
        TraceEvent("AdaptiveSlot", 4, "after-Mount", ""),
        TraceEvent("AdaptiveText", 6, "before-Mount", ""),
        TraceEvent("AdaptiveSequence", 7, "before-Mount", ""),
        TraceEvent("AdaptiveSequence", 7, "after-Mount", ""),
        TraceEvent("AdaptiveText", 6, "after-Mount", ""),
        TraceEvent("AdaptiveText", 8, "before-Mount", ""),
        TraceEvent("AdaptiveSequence", 9, "before-Mount", ""),
        TraceEvent("AdaptiveSequence", 9, "after-Mount", ""),
        TraceEvent("AdaptiveText", 8, "after-Mount", ""),
        TraceEvent("AdaptiveSequence", 3, "after-Mount", ""),
        TraceEvent("<root>", 2, "after-Mount", ""),
        TraceEvent("AdaptiveSlot", 4, "setContent", "origin: AdaptiveRootDetach1113 @ 2, detachIndex: 4"),
        TraceEvent("AdaptiveT0", 5, "before-Unmount", ""),
        TraceEvent("AdaptiveT0", 5, "after-Unmount", ""),
        TraceEvent("AdaptiveT0", 5, "before-Dispose", ""),
        TraceEvent("AdaptiveT0", 5, "after-Dispose", ""),
        TraceEvent("AdaptiveT1", 10, "before-Create", ""),
        TraceEvent("AdaptiveT1", 10, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveT1", 10, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
        TraceEvent("AdaptiveT1", 10, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
        TraceEvent("AdaptiveT1", 10, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
        TraceEvent("AdaptiveT1", 10, "after-Create", ""),
        TraceEvent("AdaptiveT1", 10, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 10, "after-Mount", ""),
        TraceEvent("AdaptiveSlot", 4, "setContent", "origin: AdaptiveRootDetach1113 @ 2, detachIndex: 6"),
        TraceEvent("AdaptiveT1", 10, "before-Unmount", ""),
        TraceEvent("AdaptiveT1", 10, "after-Unmount", ""),
        TraceEvent("AdaptiveT1", 10, "before-Dispose", ""),
        TraceEvent("AdaptiveT1", 10, "after-Dispose", ""),
        TraceEvent("AdaptiveT1", 11, "before-Create", ""),
        TraceEvent("AdaptiveT1", 11, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveT1", 11, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [23]"),
        TraceEvent("AdaptiveT1", 11, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [23]"),
        TraceEvent("AdaptiveT1", 11, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [23]"),
        TraceEvent("AdaptiveT1", 11, "after-Create", ""),
        TraceEvent("AdaptiveT1", 11, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 11, "after-Mount", "")
    ))
}