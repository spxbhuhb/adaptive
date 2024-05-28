/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package stuff

import hu.simplexion.adaptive.foundation.instruction.*
import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.foundation.testing.*
import hu.simplexion.adaptive.foundation.structural.*

fun box() : String {
    return "OK"
}

//
//@Adaptive
//fun text(content : String, vararg instructions : AdaptiveInstruction) {
//
//}
//
//class Replace(
//    @AdaptiveDetach val slotEntry: (handler: DetachHandler) -> Unit
//) : DetachHandler, AdaptiveInstruction {
//
//    fun execute() {
//        slotEntry(this)
//    }
//
//    override fun detach(origin: AdaptiveFragment, detachIndex: Int) {
//        origin.single<AdaptiveSlot>().replace(origin, detachIndex)
//    }
//
//    override fun toString(): String {
//        return "Replace"
//    }
//}
//
//fun replace(
//    @AdaptiveDetach slotEntry: (handler: DetachHandler) -> Unit
//) = Replace(slotEntry)
//
//fun box(): String {
//
//    val adapter = AdaptiveTestAdapter()
//
//    adaptive(adapter) {
//        slot()
//        text("Label", Replace { T1(12) })
//        text("Label", replace { T1(23) })
//    }
//
//    val ri = adapter.rootFragment.filter { it.instructions.firstOrNull() is Replace }.first().instructions.first() as Replace
//    ri.execute()
//
//    val ri2 = adapter.rootFragment.filter { it.instructions.firstOrNull() is Replace }[1].instructions.first() as Replace
//    ri2.execute()
//
//    return return adapter.assert(listOf(
//        TraceEvent("<root>", 2, "before-Create", ""),
//        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
//        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
//        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
//        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
//        TraceEvent("AdaptiveSequence", 3, "before-Create", ""),
//        TraceEvent("AdaptiveSequence", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
//        TraceEvent("AdaptiveSequence", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, 2, 4]"),
//        TraceEvent("AdaptiveSequence", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, 2, 4]"),
//        TraceEvent("AdaptiveSlot", 4, "before-Create", ""),
//        TraceEvent("AdaptiveSlot", 4, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
//        TraceEvent("AdaptiveSlot", 4, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
//        TraceEvent("AdaptiveSlot", 4, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
//        TraceEvent("AdaptiveSlot", 4, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
//        TraceEvent("AdaptiveSlot", 4, "after-Create", ""),
//        TraceEvent("AdaptiveText", 5, "before-Create", ""),
//        TraceEvent("AdaptiveText", 5, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
//        TraceEvent("AdaptiveText", 5, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [Label, [Replace]]"),
//        TraceEvent("AdaptiveText", 5, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [Label, [Replace]]"),
//        TraceEvent("AdaptiveText", 5, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [Label, [Replace]]"),
//        TraceEvent("AdaptiveSequence", 6, "before-Create", ""),
//        TraceEvent("AdaptiveSequence", 6, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
//        TraceEvent("AdaptiveSequence", 6, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
//        TraceEvent("AdaptiveSequence", 6, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
//        TraceEvent("AdaptiveSequence", 6, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
//        TraceEvent("AdaptiveSequence", 6, "after-Create", ""),
//        TraceEvent("AdaptiveText", 5, "after-Create", ""),
//        TraceEvent("AdaptiveText", 7, "before-Create", ""),
//        TraceEvent("AdaptiveText", 7, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
//        TraceEvent("AdaptiveText", 7, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [Label, [Replace]]"),
//        TraceEvent("AdaptiveText", 7, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [Label, [Replace]]"),
//        TraceEvent("AdaptiveText", 7, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [Label, [Replace]]"),
//        TraceEvent("AdaptiveSequence", 8, "before-Create", ""),
//        TraceEvent("AdaptiveSequence", 8, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
//        TraceEvent("AdaptiveSequence", 8, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
//        TraceEvent("AdaptiveSequence", 8, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
//        TraceEvent("AdaptiveSequence", 8, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
//        TraceEvent("AdaptiveSequence", 8, "after-Create", ""),
//        TraceEvent("AdaptiveText", 7, "after-Create", ""),
//        TraceEvent("AdaptiveSequence", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [1, 2, 4]"),
//        TraceEvent("AdaptiveSequence", 3, "after-Create", ""),
//        TraceEvent("<root>", 2, "after-Create", ""),
//        TraceEvent("<root>", 2, "before-Mount", ""),
//        TraceEvent("AdaptiveSequence", 3, "before-Mount", ""),
//        TraceEvent("AdaptiveSlot", 4, "before-Mount", ""),
//        TraceEvent("AdaptiveSlot", 4, "after-Mount", ""),
//        TraceEvent("AdaptiveText", 5, "before-Mount", ""),
//        TraceEvent("AdaptiveSequence", 6, "before-Mount", ""),
//        TraceEvent("AdaptiveSequence", 6, "after-Mount", ""),
//        TraceEvent("AdaptiveText", 5, "after-Mount", ""),
//        TraceEvent("AdaptiveText", 7, "before-Mount", ""),
//        TraceEvent("AdaptiveSequence", 8, "before-Mount", ""),
//        TraceEvent("AdaptiveSequence", 8, "after-Mount", ""),
//        TraceEvent("AdaptiveText", 7, "after-Mount", ""),
//        TraceEvent("AdaptiveSequence", 3, "after-Mount", ""),
//        TraceEvent("<root>", 2, "after-Mount", ""),
//        TraceEvent("AdaptiveT1", 9, "before-Create", ""),
//        TraceEvent("AdaptiveT1", 9, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
//        TraceEvent("AdaptiveT1", 9, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
//        TraceEvent("AdaptiveT1", 9, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
//        TraceEvent("AdaptiveT1", 9, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
//        TraceEvent("AdaptiveT1", 9, "after-Create", ""),
//        TraceEvent("AdaptiveT1", 9, "before-Mount", ""),
//        TraceEvent("AdaptiveT1", 9, "after-Mount", ""),
//        TraceEvent("AdaptiveT1", 9, "before-Unmount", ""),
//        TraceEvent("AdaptiveT1", 9, "after-Unmount", ""),
//        TraceEvent("AdaptiveT1", 9, "before-Dispose", ""),
//        TraceEvent("AdaptiveT1", 9, "after-Dispose", ""),
//        TraceEvent("AdaptiveT1", 10, "before-Create", ""),
//        TraceEvent("AdaptiveT1", 10, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
//        TraceEvent("AdaptiveT1", 10, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [23]"),
//        TraceEvent("AdaptiveT1", 10, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [23]"),
//        TraceEvent("AdaptiveT1", 10, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [23]"),
//        TraceEvent("AdaptiveT1", 10, "after-Create", ""),
//        TraceEvent("AdaptiveT1", 10, "before-Mount", ""),
//        TraceEvent("AdaptiveT1", 10, "after-Mount", "")
//    ))
//}