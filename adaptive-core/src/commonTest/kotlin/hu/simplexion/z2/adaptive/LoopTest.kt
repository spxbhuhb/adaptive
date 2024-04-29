/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

import hu.simplexion.z2.adaptive.structural.AdaptiveLoop
import hu.simplexion.z2.adaptive.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * ```kotlin
 * fun Adaptive.loop(count : Int) {
 *     for (i in 0 .. count) {
 *         T1(i + 10)
 *     }
 * }
 * ```
 */
class LoopTest {

    @Test
    fun test() {
        val adapter = AdaptiveTestAdapter()
        val root = AdaptiveTestBridge(1)

        AdaptiveLoopTest(adapter, null, 0).apply {
            state[0] = 3 // count
            create()
            mount(root)
        }

        assertEquals(
            adapter.expected(
                listOf(
                    TraceEvent("AdaptiveLoopTest", 2, "before-Create", ""),
                    TraceEvent("AdaptiveLoopTest", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [3]"),
                    TraceEvent("AdaptiveLoopTest", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [3]"),
                    TraceEvent("AdaptiveLoopTest", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [3]"),
                    TraceEvent("AdaptiveLoopTest", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [3]"),
                    TraceEvent("AdaptiveLoop", 3, "before-Create", ""),
                    TraceEvent("AdaptiveLoop", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,null]"),
                    TraceEvent("AdaptiveLoop", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [IntProgressionIterator,AdaptiveFragmentFactory(2,1)]"),
                    TraceEvent("AdaptiveLoop", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [IntProgressionIterator,AdaptiveFragmentFactory(2,1)]"),
                    TraceEvent("AdaptiveAnonymous", 5, "before-Create", ""),
                    TraceEvent("AdaptiveAnonymous", 5, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [0]"),
                    TraceEvent("AdaptiveAnonymous", 5, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [0]"),
                    TraceEvent("AdaptiveAnonymous", 5, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [0]"),
                    TraceEvent("AdaptiveAnonymous", 5, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [0]"),
                    TraceEvent("AdaptiveT1", 6, "before-Create", ""),
                    TraceEvent("AdaptiveT1", 6, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveT1", 6, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [10]"),
                    TraceEvent("AdaptiveT1", 6, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [10]"),
                    TraceEvent("AdaptiveT1", 6, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [10]"),
                    TraceEvent("AdaptiveT1", 6, "after-Create", ""),
                    TraceEvent("AdaptiveAnonymous", 5, "after-Create", ""),
                    TraceEvent("AdaptiveAnonymous", 5, "before-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 6, "before-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 6, "after-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveAnonymous", 5, "after-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveAnonymous", 7, "before-Create", ""),
                    TraceEvent("AdaptiveAnonymous", 7, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [1]"),
                    TraceEvent("AdaptiveAnonymous", 7, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [1]"),
                    TraceEvent("AdaptiveAnonymous", 7, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [1]"),
                    TraceEvent("AdaptiveAnonymous", 7, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [1]"),
                    TraceEvent("AdaptiveT1", 8, "before-Create", ""),
                    TraceEvent("AdaptiveT1", 8, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveT1", 8, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [11]"),
                    TraceEvent("AdaptiveT1", 8, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [11]"),
                    TraceEvent("AdaptiveT1", 8, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [11]"),
                    TraceEvent("AdaptiveT1", 8, "after-Create", ""),
                    TraceEvent("AdaptiveAnonymous", 7, "after-Create", ""),
                    TraceEvent("AdaptiveAnonymous", 7, "before-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 8, "before-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 8, "after-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveAnonymous", 7, "after-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveAnonymous", 9, "before-Create", ""),
                    TraceEvent("AdaptiveAnonymous", 9, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [2]"),
                    TraceEvent("AdaptiveAnonymous", 9, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [2]"),
                    TraceEvent("AdaptiveAnonymous", 9, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [2]"),
                    TraceEvent("AdaptiveAnonymous", 9, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [2]"),
                    TraceEvent("AdaptiveT1", 10, "before-Create", ""),
                    TraceEvent("AdaptiveT1", 10, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveT1", 10, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
                    TraceEvent("AdaptiveT1", 10, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
                    TraceEvent("AdaptiveT1", 10, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
                    TraceEvent("AdaptiveT1", 10, "after-Create", ""),
                    TraceEvent("AdaptiveAnonymous", 9, "after-Create", ""),
                    TraceEvent("AdaptiveAnonymous", 9, "before-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 10, "before-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 10, "after-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveAnonymous", 9, "after-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveAnonymous", 11, "before-Create", ""),
                    TraceEvent("AdaptiveAnonymous", 11, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [3]"),
                    TraceEvent("AdaptiveAnonymous", 11, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [3]"),
                    TraceEvent("AdaptiveAnonymous", 11, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [3]"),
                    TraceEvent("AdaptiveAnonymous", 11, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [3]"),
                    TraceEvent("AdaptiveT1", 12, "before-Create", ""),
                    TraceEvent("AdaptiveT1", 12, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveT1", 12, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [13]"),
                    TraceEvent("AdaptiveT1", 12, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [13]"),
                    TraceEvent("AdaptiveT1", 12, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [13]"),
                    TraceEvent("AdaptiveT1", 12, "after-Create", ""),
                    TraceEvent("AdaptiveAnonymous", 11, "after-Create", ""),
                    TraceEvent("AdaptiveAnonymous", 11, "before-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 12, "before-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 12, "after-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveAnonymous", 11, "after-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveLoop", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [IntProgressionIterator,AdaptiveFragmentFactory(2,1)]"),
                    TraceEvent("AdaptiveLoop", 3, "after-Create", ""),
                    TraceEvent("AdaptiveLoopTest", 2, "after-Create", ""),
                    TraceEvent("AdaptiveLoopTest", 2, "before-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveLoop", 3, "before-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveAnonymous", 5, "before-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 6, "before-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 6, "after-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveAnonymous", 5, "after-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveAnonymous", 7, "before-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 8, "before-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 8, "after-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveAnonymous", 7, "after-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveAnonymous", 9, "before-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 10, "before-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 10, "after-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveAnonymous", 9, "after-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveAnonymous", 11, "before-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 12, "before-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 12, "after-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveAnonymous", 11, "after-Mount", "bridge: 4"),
                    TraceEvent("AdaptiveLoop", 3, "after-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveLoopTest", 2, "after-Mount", "bridge: 1")
                )
            ),
            adapter.actual(dumpCode = false)
        )
    }
}

class AdaptiveLoopTest(
    adapter: AdaptiveAdapter<TestNode>,
    parent: AdaptiveFragment<TestNode>?,
    index: Int
) : AdaptiveFragment<TestNode>(adapter, parent, index, 1) {

    var count : Int
        get() = state[0] as Int
        set(v) { state[0] = v }

    val dependencyMask_0_0 = 0x01 // fragment index: 0, state variable index: 0
    val dependencyMask_0_1 = 0x02 // fragment index: 0, state variable index: 0
    val dependencyMask_1_0 = 0x02 // fragment index: 1, state variable index: 0

    override fun genBuild(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode> {
        val fragment = when (declarationIndex) {
            0 -> AdaptiveLoop<TestNode, Int>(adapter, parent, declarationIndex)
            1 -> AdaptiveT1(adapter, parent, declarationIndex)
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
                    fragment.setStateVariable(0, (0 .. (this.getThisClosureVariable(0) as Int)).iterator())
                }
                if (fragment.haveToPatch(closureMask, dependencyMask_0_1)) {
                    fragment.setStateVariable(1, AdaptiveFragmentFactory(this, 1))
                }
            }
            1 -> {
                // T1.createClosure is [ (count), (i) ]
                if (fragment.haveToPatch(closureMask, dependencyMask_1_0)) {
                    fragment.setStateVariable(0, (fragment.getCreateClosureVariable(1) as Int) + 10)
                }
            }
            else -> invalidIndex(fragment.index)
        }
    }

    override fun genPatchInternal() {

    }

}