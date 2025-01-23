/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.foundation.*
import `fun`.adaptive.foundation.binding.*
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.testing.*

@Adaptive
fun accessTest() {
    val a = 12
    accessor { a }
}

@Adaptive
fun <T> accessor(
    binding: AdaptiveStateVariableBinding<T>? = null,
    @Suppress("UNUSED_PARAMETER")
    @PropertySelector
    selector: () -> T
) {
    checkNotNull(binding)
    T1(binding.value as Int)
}

fun box() : String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        accessTest()
    }

    return adapter.assert(listOf(
        //@formatter:off
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null]"),
        TraceEvent("AdaptiveAccessTest", 3, "before-Create", ""),
        TraceEvent("AdaptiveAccessTest", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveAccessTest", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveAccessTest", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveAccessTest", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 12]"),
        TraceEvent("AdaptiveAccessor", 4, "before-Create", ""),
        TraceEvent("AdaptiveAccessor", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveAccessTest", 3, "before-Add-Binding", "binding: AdaptiveStateVariableBinding(3, 1, 1, 4, 1, null, I, null)"),
        TraceEvent("AdaptiveAccessTest", 3, "after-Add-Binding", "binding: AdaptiveStateVariableBinding(3, 1, 1, 4, 1, null, I, null)"),
        TraceEvent("AdaptiveAccessor", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, AdaptiveStateVariableBinding(3, 1, 1, 4, 1, null, I, null)]"),
        TraceEvent("AdaptiveAccessor", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, AdaptiveStateVariableBinding(3, 1, 1, 4, 1, null, I, null)]"),
        TraceEvent("AdaptiveAccessor", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, AdaptiveStateVariableBinding(3, 1, 1, 4, 1, null, I, null)]"),
        TraceEvent("AdaptiveT1", 5, "before-Create", ""),
        TraceEvent("AdaptiveT1", 5, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12]"),
        TraceEvent("AdaptiveT1", 5, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 12]"),
        TraceEvent("AdaptiveT1", 5, "after-Create", ""),
        TraceEvent("AdaptiveAccessor", 4, "after-Create", ""),
        TraceEvent("AdaptiveAccessTest", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", ""),
        TraceEvent("AdaptiveAccessTest", 3, "before-Mount", ""),
        TraceEvent("AdaptiveAccessor", 4, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 5, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 5, "after-Mount", ""),
        TraceEvent("AdaptiveAccessor", 4, "after-Mount", ""),
        TraceEvent("AdaptiveAccessTest", 3, "after-Mount", ""),
        TraceEvent("<root>", 2, "after-Mount", "")
        //@formatter:on
    ))
}