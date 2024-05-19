/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.base

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.adapter
import hu.simplexion.adaptive.foundation.internal.BoundSupportFunction
import hu.simplexion.adaptive.foundation.internal.initStateMask
import hu.simplexion.adaptive.foundation.testing.*
import hu.simplexion.adaptive.foundation.producer.cancelProducer
import hu.simplexion.adaptive.foundation.producer.poll
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration

var counter = 12

@Suppress("unused")
fun pollTest() {
    val i = poll(Duration.ZERO, 2) {
        if (counter < 14) {
            counter++
        } else {
            (adapter() as AdaptiveTestAdapter).done = true
            cancelProducer()
        }
    }
    T1(i)
}

@Suppress("OPT_IN_USAGE")
class PollTest {

    @Test
    fun test() {
        val adapter = AdaptiveTestAdapter()

        adapter.dispatcher = newSingleThreadContext("test thread")

        runBlocking(adapter.dispatcher) {
            AdaptivePollTest(adapter, null, 0).apply {
                create()
                mount()
            }

            adapter.waitFor()

            assertEquals(
                adapter.expected(
                    listOf(
                        //@formatter:off
                        TraceEvent("AdaptivePollTest", 2, "before-Create", ""),
                        TraceEvent("AdaptivePollTest", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
                        TraceEvent("AdaptivePollTest", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
                        TraceEvent("AdaptivePollTest", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
                        TraceEvent("AdaptivePollTest", 2, "before-Add-Producer", "producer: AdaptivePoll(AdaptiveStateVariableBinding(2, 0, 0, 2, 0, null, 0, AdaptivePropertyMetadata(kotlin.int)), 0s)"),
                        TraceEvent("AdaptivePollTest", 2, "after-Add-Producer", "producer: AdaptivePoll(AdaptiveStateVariableBinding(2, 0, 0, 2, 0, null, 0, AdaptivePropertyMetadata(kotlin.int)), 0s)"),
                        TraceEvent("AdaptivePollTest", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [2]"),
                        TraceEvent("AdaptiveT1", 3, "before-Create", ""),
                        TraceEvent("AdaptiveT1", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                        TraceEvent("AdaptiveT1", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [2]"),
                        TraceEvent("AdaptiveT1", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [2]"),
                        TraceEvent("AdaptiveT1", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [2]"),
                        TraceEvent("AdaptiveT1", 3, "after-Create", ""),
                        TraceEvent("AdaptivePollTest", 2, "after-Create", ""),
                        TraceEvent("AdaptivePollTest", 2, "before-Mount"),
                        TraceEvent("AdaptiveT1", 3, "before-Mount"),
                        TraceEvent("AdaptiveT1", 3, "after-Mount"),
                        TraceEvent("AdaptivePollTest", 2, "after-Mount"),
                        TraceEvent("AdaptivePollTest", 2, "before-Invoke-Suspend", "BoundSupportFunction(2, 2, 0) arguments: []"),
                        TraceEvent("AdaptivePollTest", 2, "after-Invoke-Suspend", "index: 0 result: 12"),
                        TraceEvent("AdaptivePollTest", 2, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [12]"),
                        TraceEvent("AdaptiveT1", 3, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [2]"),
                        TraceEvent("AdaptiveT1", 3, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [12]"),
                        TraceEvent("AdaptiveT1", 3, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [12]"),
                        TraceEvent("AdaptiveT1", 3, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [12]"),
                        TraceEvent("AdaptivePollTest", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
                        TraceEvent("AdaptivePollTest", 2, "before-Invoke-Suspend", "BoundSupportFunction(2, 2, 0) arguments: []"),
                        TraceEvent("AdaptivePollTest", 2, "after-Invoke-Suspend", "index: 0 result: 13"),
                        TraceEvent("AdaptivePollTest", 2, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [13]"),
                        TraceEvent("AdaptiveT1", 3, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [12]"),
                        TraceEvent("AdaptiveT1", 3, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [13]"),
                        TraceEvent("AdaptiveT1", 3, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [13]"),
                        TraceEvent("AdaptiveT1", 3, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [13]"),
                        TraceEvent("AdaptivePollTest", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [13]"),
                        TraceEvent("AdaptivePollTest", 2, "before-Invoke-Suspend", "BoundSupportFunction(2, 2, 0) arguments: []")
                        //@formatter:on
                    )
                ),
                adapter.actual(dumpCode = false)
            )
        }
    }
}

class AdaptivePollTest(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int
) : AdaptiveTestFragment(adapter, parent, index, 1) {

    val dependencyMask_0_0 = 0x01 // fragment index: 0, state variable index: 0

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment {

        val fragment = when (declarationIndex) {
            0 -> AdaptiveT1(adapter, parent, declarationIndex)
            else -> invalidIndex(declarationIndex) // throws exception
        }

        fragment.create()

        return fragment
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {

        val closureMask = fragment.getCreateClosureDirtyMask()

        when (fragment.declarationIndex) {
            0 -> {
                if (fragment.haveToPatch(closureMask, dependencyMask_0_0)) {
                    fragment.setStateVariable(0, this.getThisClosureVariable(0))
                }
            }
        }
    }

    override fun genPatchInternal(): Boolean {
        if (getThisClosureDirtyMask() == initStateMask) {
            this.setStateVariable(0, poll(Duration.ZERO, 2, localBinding(0, 0, "kotlin.int"), null))
        }
        return true
    }

    @Suppress("RedundantNullableReturnType")
    override suspend fun genInvokeSuspend(
        supportFunction: BoundSupportFunction,
        arguments: Array<out Any?>
    ): Any? {

        return when (supportFunction.supportFunctionIndex) {
            0 -> {
                if (counter < 14) {
                    counter++
                } else {
                    (adapter as AdaptiveTestAdapter).done = true
                    cancelProducer()
                }
            }
            else -> invalidIndex(supportFunction.supportFunctionIndex)
        }

    }

}