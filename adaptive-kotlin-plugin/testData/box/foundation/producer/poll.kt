/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.*
import `fun`.adaptive.foundation.testing.*
import `fun`.adaptive.foundation.producer.*
import `fun`.adaptive.service.testing.TestServiceTransport
import kotlinx.coroutines.*
import kotlin.time.Duration

var counter = 12

@Adaptive
fun pollTest() {
    val i = poll(Duration.ZERO) {
        if (counter < 14) {
            // this is counter++ but ++ is not supported yet
            counter = counter + 1
            counter - 1
        } else {
            (adapter() as AdaptiveTestAdapter).done = true
            cancelProducer()
        }
    } ?: 2
    T1(i)
}

@Suppress("OPT_IN_USAGE")
fun box(): String {

    val dispatcher = newSingleThreadContext("test thread")

    val adapter = AdaptiveTestAdapter(
        backend = BackendAdapter(
            dispatcher = dispatcher,
            scope = CoroutineScope(dispatcher),
            transport = TestServiceTransport()
        )
    )

    try {

        runBlocking(adapter.dispatcher) {

            adaptive(adapter) {
                pollTest()
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
            TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null]"),
            TraceEvent("AdaptivePollTest", 3, "before-Create", ""),
            TraceEvent("AdaptivePollTest", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("AdaptivePollTest", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("AdaptivePollTest", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("AdaptivePollTest", 3, "before-Add-Producer", "producer: AdaptivePoll(AdaptiveStateVariableBinding(3, 1, 1, 3, 1, null, kotlin.Int, null), 0s)"),
            TraceEvent("AdaptivePollTest", 3, "after-Add-Producer", "producer: AdaptivePoll(AdaptiveStateVariableBinding(3, 1, 1, 3, 1, null, kotlin.Int, null), 0s)"),
            TraceEvent("AdaptivePollTest", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 2]"),
            TraceEvent("AdaptiveT1", 4, "before-Create", ""),
            TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
            TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 2]"),
            TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 2]"),
            TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 2]"),
            TraceEvent("AdaptiveT1", 4, "after-Create", ""),
            TraceEvent("AdaptivePollTest", 3, "after-Create", ""),
            TraceEvent("<root>", 2, "after-Create", ""),
            TraceEvent("<root>", 2, "before-Mount", ""),
            TraceEvent("AdaptivePollTest", 3, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 4, "before-Mount", ""),
            TraceEvent("AdaptiveT1", 4, "after-Mount", ""),
            TraceEvent("AdaptivePollTest", 3, "after-Mount", ""),
            TraceEvent("<root>", 2, "after-Mount", ""),
            TraceEvent("AdaptivePollTest", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000002 state: [null, 2]"),
            TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, 2]"),
            TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, 12]"),
            TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, 12]"),
            TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, 12]"),
            TraceEvent("AdaptivePollTest", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 12]"),
            TraceEvent("AdaptivePollTest", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000002 state: [null, 12]"),
            TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, 12]"),
            TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, 13]"),
            TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000002 state: [null, 13]"),
            TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000002 thisMask: 0x00000000 state: [null, 13]"),
            TraceEvent("AdaptivePollTest", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 13]")
            //@formatter:on
        )
    )
}