/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adaptive
import `fun`.adaptive.foundation.testing.*

//@Adaptive
//fun <T> loading(
//    data: T?,
//    @Adaptive
//    content: (T) -> Unit
//) {
//    content(data)
//}

fun box() : String {

    // this
//    val adapter = AdaptiveTestAdapter()
//
//    adaptive(adapter) {
//        loading(listOf(1,2,3)) { numbers ->
//            for(i in numbers) {
//                T1(i)
//            }
//        }
//    }
//
//    return adapter.assert(listOf(
//        TraceEvent("<root>", 2, "before-Create", ""),
//        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
//        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
//        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
//        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null]"),
//        TraceEvent("AdaptiveSequence", 3, "before-Create", ""),
//        TraceEvent("AdaptiveSequence", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,null]"),
//        TraceEvent("AdaptiveSequence", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,[1, 2]]"),
//        TraceEvent("AdaptiveSequence", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,[1, 2]]"),
//        TraceEvent("AdaptiveWithDefault", 4, "before-Create", ""),
//        TraceEvent("AdaptiveWithDefault", 4, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
//        TraceEvent("AdaptiveWithDefault", 4, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
//        TraceEvent("AdaptiveWithDefault", 4, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
//        TraceEvent("AdaptiveWithDefault", 4, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [null, 12]"),
//        TraceEvent("AdaptiveT1", 5, "before-Create", ""),
//        TraceEvent("AdaptiveT1", 5, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
//        TraceEvent("AdaptiveT1", 5, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12]"),
//        TraceEvent("AdaptiveT1", 5, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12]"),
//        TraceEvent("AdaptiveT1", 5, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 12]"),
//        TraceEvent("AdaptiveT1", 5, "after-Create", ""),
//        TraceEvent("AdaptiveWithDefault", 4, "after-Create", ""),
//        TraceEvent("AdaptiveWithDefault", 6, "before-Create", ""),
//        TraceEvent("AdaptiveWithDefault", 6, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
//        TraceEvent("AdaptiveWithDefault", 6, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, 23]"),
//        TraceEvent("AdaptiveWithDefault", 6, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, 23]"),
//        TraceEvent("AdaptiveWithDefault", 6, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [null, 23]"),
//        TraceEvent("AdaptiveT1", 7, "before-Create", ""),
//        TraceEvent("AdaptiveT1", 7, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
//        TraceEvent("AdaptiveT1", 7, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 23]"),
//        TraceEvent("AdaptiveT1", 7, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 23]"),
//        TraceEvent("AdaptiveT1", 7, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 23]"),
//        TraceEvent("AdaptiveT1", 7, "after-Create", ""),
//        TraceEvent("AdaptiveWithDefault", 6, "after-Create", ""),
//        TraceEvent("AdaptiveSequence", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null,[1, 2]]"),
//        TraceEvent("AdaptiveSequence", 3, "after-Create", ""),
//        TraceEvent("<root>", 2, "after-Create", ""),
//        TraceEvent("<root>", 2, "before-Mount", ""),
//        TraceEvent("AdaptiveSequence", 3, "before-Mount", ""),
//        TraceEvent("AdaptiveWithDefault", 4, "before-Mount", ""),
//        TraceEvent("AdaptiveT1", 5, "before-Mount", ""),
//        TraceEvent("AdaptiveT1", 5, "after-Mount", ""),
//        TraceEvent("AdaptiveWithDefault", 4, "after-Mount", ""),
//        TraceEvent("AdaptiveWithDefault", 6, "before-Mount", ""),
//        TraceEvent("AdaptiveT1", 7, "before-Mount", ""),
//        TraceEvent("AdaptiveT1", 7, "after-Mount", ""),
//        TraceEvent("AdaptiveWithDefault", 6, "after-Mount", ""),
//        TraceEvent("AdaptiveSequence", 3, "after-Mount", ""),
//        TraceEvent("<root>", 2, "after-Mount", "")
//    ))

    return "OK"
}