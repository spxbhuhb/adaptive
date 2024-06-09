/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.base.success

import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.foundation.testing.*
import hu.simplexion.adaptive.foundation.producer.*
import kotlinx.coroutines.*
import kotlin.time.Duration

var done = false
fun mock(i: Int) = i + 100

@Suppress("OPT_IN_USAGE")
fun box(): String {

    val adapter = AdaptiveTestAdapter()
    adapter.dispatcher = newSingleThreadContext("test thread")

    try {

        runBlocking(adapter.dispatcher) {

            adaptive(adapter) {
                val i = 12
                val j = poll(Duration.ZERO, i + 1) {
                    if (done) {
                        (adapter() as AdaptiveTestAdapter).done = true
                        cancelProducer()
                    } else {
                        done = true
                        mock(i)
                    }
                }
                T1(j)
            }

            adapter.waitFor()
        }

    } finally {
        (adapter.dispatcher as CloseableCoroutineDispatcher).close()
    }

    return adapter.assert(
        listOf(
            //@formatter:off
            TraceEvent("<root>", 2, "before-Create", ""),
            TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("<root>", 2, "before-Add-Producer", "producer: AdaptivePoll(AdaptiveStateVariableBinding(2, 1, 1, 2, 1, null, AdaptivePropertyMetadata(kotlin.Int)), 0s)"),
            TraceEvent("<root>", 2, "after-Add-Producer", "producer: AdaptivePoll(AdaptiveStateVariableBinding(2, 1, 1, 2, 1, null, AdaptivePropertyMetadata(kotlin.Int)), 0s)"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12, 13]"),
            TraceEvent("AdaptiveT1", 3, "before-Create", ""),
            TraceEvent("AdaptiveT1", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveT1", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [13]"),
            TraceEvent("AdaptiveT1", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [13]"),
            TraceEvent("AdaptiveT1", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [13]"),
            TraceEvent("AdaptiveT1", 3, "after-Create", ""),
            TraceEvent("<root>", 2, "after-Create", ""),
            TraceEvent("<root>", 2, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 3, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 3, "after-Mount", ""),
            TraceEvent("<root>", 2, "after-Mount", ""),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000002 state: [12, 112]"),
            TraceEvent("AdaptiveT1", 3, "before-Patch-External", "createMask: 0x00000002 thisMask: 0x00000000 state: [13]"),
            TraceEvent("AdaptiveT1", 3, "after-Patch-External", "createMask: 0x00000002 thisMask: 0x00000001 state: [112]"),
            TraceEvent("AdaptiveT1", 3, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000001 state: [112]"),
            TraceEvent("AdaptiveT1", 3, "after-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000000 state: [112]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12, 112]")
            //@formatter:on
        )
    )
}