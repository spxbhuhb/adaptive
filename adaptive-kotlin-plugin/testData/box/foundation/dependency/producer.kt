/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.foundation.*
import `fun`.adaptive.foundation.testing.*
import `fun`.adaptive.foundation.producer.*
import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.store.copyStore

@Adat
class T(
    val ti: Int
)

@Adaptive
fun copyStoreTest(pi: Int) {

    @Independent
    val t = copyStore { T(pi + 2) }

    T1(t.ti)
}

fun box(): String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        val ri = 12
        copyStoreTest(ri)
    }

    adapter.rootFragment.setStateVariable(1, 23)
    adapter.rootFragment.children.first().patch()

    return adapter.assert(
        listOf(
            //@formatter:off
            TraceEvent("<root>", 2, "before-Create", ""),
            TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 12]"),
            TraceEvent("AdaptiveCopyStoreTest", 3, "before-Create", ""),
            TraceEvent("AdaptiveCopyStoreTest", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null, null]"),
            TraceEvent("AdaptiveCopyStoreTest", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12, null]"),
            TraceEvent("AdaptiveCopyStoreTest", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12, null]"),
            TraceEvent("AdaptiveCopyStoreTest", 3, "before-Add-Producer", "producer: CopyStore(AdaptiveStateVariableBinding(3, 2, 2, 3, 2, null, fun.adaptive.kotlin.base.success.T, fun.adaptive.kotlin.base.success.T))"),
            TraceEvent("AdaptiveCopyStoreTest", 3, "after-Add-Producer", "producer: CopyStore(AdaptiveStateVariableBinding(3, 2, 2, 3, 2, null, fun.adaptive.kotlin.base.success.T, fun.adaptive.kotlin.base.success.T))"),
            TraceEvent("AdaptiveCopyStoreTest", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 12, T(ti=14)]"),
            TraceEvent("AdaptiveT1", 4, "before-Create", ""),
            TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 14]"),
            TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 14]"),
            TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 14]"),
            TraceEvent("AdaptiveT1", 4, "after-Create", ""),
            TraceEvent("AdaptiveCopyStoreTest", 3, "after-Create", ""),
            TraceEvent("<root>", 2, "after-Create", ""),
            TraceEvent("<root>", 2, "before-Mount", ""),
            TraceEvent("AdaptiveCopyStoreTest", 3, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 4, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 4, "after-Mount", ""),
            TraceEvent("AdaptiveCopyStoreTest", 3, "after-Mount", ""),
            TraceEvent("<root>", 2, "after-Mount", ""),
            TraceEvent("AdaptiveCopyStoreTest", 3, "before-Patch-External", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, 12, T(ti=14)]"),
            TraceEvent("AdaptiveCopyStoreTest", 3, "after-Patch-External", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, 23, T(ti=14)]"),
            TraceEvent("AdaptiveCopyStoreTest", 3, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, 23, T(ti=14)]"),
            TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, 14]"),
            TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, 14]"),
            TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, 14]"),
            TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, 14]"),
            TraceEvent("AdaptiveCopyStoreTest", 3, "after-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, 23, T(ti=14)]")
            //@formatter:on
        )
    )
}