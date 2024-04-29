/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

import hu.simplexion.z2.adaptive.structural.AdaptiveSequence
import hu.simplexion.z2.adaptive.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * fun Adaptive.sequenceTest() {
 *     T0()
 *     T1(12)
 * }
 */
class SequenceTest {

    @Test
    fun test() {
        val adapter = AdaptiveTestAdapter()
        val root = AdaptiveTestBridge(1)

        AdaptiveSequenceTest(adapter, null, 0).apply {
            create()
            mount(root)
        }

        assertEquals(
            adapter.expected(
                listOf(
                    TraceEvent("AdaptiveSequenceTest", 2, "before-Create", ""),
                    TraceEvent("AdaptiveSequenceTest", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
                    TraceEvent("AdaptiveSequenceTest", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
                    TraceEvent("AdaptiveSequenceTest", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
                    TraceEvent("AdaptiveSequenceTest", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
                    TraceEvent("AdaptiveSequence", 3, "before-Create", ""),
                    TraceEvent("AdaptiveSequence", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
                    TraceEvent("AdaptiveSequence", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, 2]"),
                    TraceEvent("AdaptiveSequence", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, 2]"),
                    TraceEvent("AdaptiveSequence", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [1, 2]"),
                    TraceEvent("AdaptiveT0", 4, "before-Create", ""),
                    TraceEvent("AdaptiveT0", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
                    TraceEvent("AdaptiveT0", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
                    TraceEvent("AdaptiveT0", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
                    TraceEvent("AdaptiveT0", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
                    TraceEvent("AdaptiveT0", 4, "after-Create", ""),
                    TraceEvent("AdaptiveT1", 5, "before-Create", ""),
                    TraceEvent("AdaptiveT1", 5, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveT1", 5, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
                    TraceEvent("AdaptiveT1", 5, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
                    TraceEvent("AdaptiveT1", 5, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
                    TraceEvent("AdaptiveT1", 5, "after-Create", ""),
                    TraceEvent("AdaptiveSequence", 3, "after-Create", ""),
                    TraceEvent("AdaptiveSequenceTest", 2, "after-Create", ""),
                    TraceEvent("AdaptiveSequenceTest", 2, "before-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveSequence", 3, "before-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveT0", 4, "before-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveT0", 4, "after-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveT1", 5, "before-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveT1", 5, "after-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveSequence", 3, "after-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveSequenceTest", 2, "after-Mount", "bridge: 1")
                )
            ),
            adapter.actual(dumpCode = false)
        )
    }
}

class AdaptiveSequenceTest(
    adapter: AdaptiveAdapter<TestNode>,
    parent: AdaptiveFragment<TestNode>?,
    index: Int
) : AdaptiveFragment<TestNode>(adapter, parent, index, 0) {

    val dependencyMask_0_0 = 0x00 // fragment index: 0, state variable index: 0
    val dependencyMask_1_0 = 0x00 // fragment index: 1, state variable index: 0

    override fun genBuild(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode> {

        val fragment = when (declarationIndex) {
            0 -> AdaptiveSequence(adapter, parent, declarationIndex)
            1 -> AdaptiveT0(adapter, parent, declarationIndex)
            2 -> AdaptiveT1(adapter, parent, declarationIndex)
            else -> invalidIndex(declarationIndex) // throws exception
        }

        fragment.create()

        return fragment
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment<TestNode>) {

        val closureMask = fragment.getCreateClosureDirtyMask()

        when (fragment.index) {
            0 -> {
                if (fragment.haveToPatch(closureMask, dependencyMask_0_0)) {
                    fragment.setStateVariable(0, intArrayOf(1, 2)) // indices of T0 and T1
                }
            }

            2 -> {
                if (fragment.haveToPatch(closureMask, dependencyMask_1_0)) {
                    fragment.setStateVariable(0, 12)
                }
            }
        }
    }

    override fun genPatchInternal() {

    }

}