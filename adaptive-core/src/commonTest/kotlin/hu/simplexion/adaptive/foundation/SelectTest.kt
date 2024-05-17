/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.foundation

import hu.simplexion.adaptive.foundation.structural.AdaptiveSelect
import hu.simplexion.adaptive.foundation.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * ```kotlin
 * fun Adaptive.selectTest(v0 : Int) {
 *     when {
 *         v0 == 0 -> Unit
 *         v0 % 2 == 0 -> T1(v0 + 10)
 *         else -> T1(v0 + 20)
 *     }
 * }
 * ```
 */
class SelectTest {

    @Test
    fun testWithEmptyStart() {
        val adapter = AdaptiveTestAdapter()

        AdaptiveSelectTest(adapter, null, 0).apply {
            v0 = 0 // start with empty

            create()
            mount()

            fun v(value: Int) {
                v0 = value
                patchInternal()
            }

            // even: + 10  odd: + 20
            v(1) // replace empty with 21
            v(2) // replace 21 with 12
            v(3) // replace 12 with 23
            v(1) // patch 23 to 21
            v(0) // replace 21 with empty
        }

        assertEquals(
            adapter.expected(
                listOf(
                    TraceEvent("AdaptiveSelectTest", 2, "before-Create", ""),
                    TraceEvent("AdaptiveSelectTest", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [0]"),
                    TraceEvent("AdaptiveSelectTest", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [0]"),
                    TraceEvent("AdaptiveSelectTest", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [0]"),
                    TraceEvent("AdaptiveSelectTest", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [0]"),
                    TraceEvent("AdaptiveSelect", 3, "before-Create", ""),
                    TraceEvent("AdaptiveSelect", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, -1]"),
                    TraceEvent("AdaptiveSelect", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [-1, -1]"),
                    TraceEvent("AdaptiveSelect", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [-1, -1]"),
                    TraceEvent("AdaptiveSelect", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [-1, -1]"),
                    TraceEvent("AdaptiveSelect", 3, "after-Create", ""),
                    TraceEvent("AdaptiveSelectTest", 2, "after-Create", ""),
                    TraceEvent("AdaptiveSelectTest", 2, "before-Mount"),
                    TraceEvent("AdaptiveSelect", 3, "before-Mount"),
                    TraceEvent("TestPlaceholder", 4, "before-Mount"),
                    TraceEvent("TestPlaceholder", 4, "after-Mount"),
                    TraceEvent("AdaptiveSelect", 3, "after-Mount"),
                    TraceEvent("AdaptiveSelectTest", 2, "after-Mount"),
                    TraceEvent("AdaptiveSelectTest", 2, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [1]"),
                    TraceEvent("AdaptiveSelect", 3, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [-1, -1]"),
                    TraceEvent("AdaptiveSelect", 3, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000003 state: [2, -1]"),
                    TraceEvent("AdaptiveSelect", 3, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000003 state: [2, -1]"),
                    TraceEvent("AdaptiveT1", 5, "before-Create", ""),
                    TraceEvent("AdaptiveT1", 5, "before-Patch-External", "createMask: 0x00000003 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveT1", 5, "after-Patch-External", "createMask: 0x00000003 thisMask: 0xffffffff state: [21]"),
                    TraceEvent("AdaptiveT1", 5, "before-Patch-Internal", "createMask: 0x00000003 thisMask: 0xffffffff state: [21]"),
                    TraceEvent("AdaptiveT1", 5, "after-Patch-Internal", "createMask: 0x00000003 thisMask: 0x00000000 state: [21]"),
                    TraceEvent("AdaptiveT1", 5, "after-Create", ""),
                    TraceEvent("AdaptiveT1", 5, "before-Mount"),
                    TraceEvent("AdaptiveT1", 5, "after-Mount"),
                    TraceEvent("AdaptiveSelect", 3, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [2, 2]"),
                    TraceEvent("AdaptiveSelectTest", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [1]"),
                    TraceEvent("AdaptiveSelectTest", 2, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [2]"),
                    TraceEvent("AdaptiveSelect", 3, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [2, 2]"),
                    TraceEvent("AdaptiveSelect", 3, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000003 state: [1, 2]"),
                    TraceEvent("AdaptiveSelect", 3, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000003 state: [1, 2]"),
                    TraceEvent("AdaptiveT1", 5, "before-Unmount"),
                    TraceEvent("AdaptiveT1", 5, "after-Unmount"),
                    TraceEvent("AdaptiveT1", 5, "before-Dispose", ""),
                    TraceEvent("AdaptiveT1", 5, "after-Dispose", ""),
                    TraceEvent("AdaptiveT1", 6, "before-Create", ""),
                    TraceEvent("AdaptiveT1", 6, "before-Patch-External", "createMask: 0x00000003 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveT1", 6, "after-Patch-External", "createMask: 0x00000003 thisMask: 0xffffffff state: [12]"),
                    TraceEvent("AdaptiveT1", 6, "before-Patch-Internal", "createMask: 0x00000003 thisMask: 0xffffffff state: [12]"),
                    TraceEvent("AdaptiveT1", 6, "after-Patch-Internal", "createMask: 0x00000003 thisMask: 0x00000000 state: [12]"),
                    TraceEvent("AdaptiveT1", 6, "after-Create", ""),
                    TraceEvent("AdaptiveT1", 6, "before-Mount"),
                    TraceEvent("AdaptiveT1", 6, "after-Mount"),
                    TraceEvent("AdaptiveSelect", 3, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [1, 1]"),
                    TraceEvent("AdaptiveSelectTest", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [2]"),
                    TraceEvent("AdaptiveSelectTest", 2, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [3]"),
                    TraceEvent("AdaptiveSelect", 3, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [1, 1]"),
                    TraceEvent("AdaptiveSelect", 3, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000003 state: [2, 1]"),
                    TraceEvent("AdaptiveSelect", 3, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000003 state: [2, 1]"),
                    TraceEvent("AdaptiveT1", 6, "before-Unmount"),
                    TraceEvent("AdaptiveT1", 6, "after-Unmount"),
                    TraceEvent("AdaptiveT1", 6, "before-Dispose", ""),
                    TraceEvent("AdaptiveT1", 6, "after-Dispose", ""),
                    TraceEvent("AdaptiveT1", 7, "before-Create", ""),
                    TraceEvent("AdaptiveT1", 7, "before-Patch-External", "createMask: 0x00000003 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveT1", 7, "after-Patch-External", "createMask: 0x00000003 thisMask: 0xffffffff state: [23]"),
                    TraceEvent("AdaptiveT1", 7, "before-Patch-Internal", "createMask: 0x00000003 thisMask: 0xffffffff state: [23]"),
                    TraceEvent("AdaptiveT1", 7, "after-Patch-Internal", "createMask: 0x00000003 thisMask: 0x00000000 state: [23]"),
                    TraceEvent("AdaptiveT1", 7, "after-Create", ""),
                    TraceEvent("AdaptiveT1", 7, "before-Mount"),
                    TraceEvent("AdaptiveT1", 7, "after-Mount"),
                    TraceEvent("AdaptiveSelect", 3, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [2, 2]"),
                    TraceEvent("AdaptiveSelectTest", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [3]"),
                    TraceEvent("AdaptiveSelectTest", 2, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [1]"),
                    TraceEvent("AdaptiveSelect", 3, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [2, 2]"),
                    TraceEvent("AdaptiveSelect", 3, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [2, 2]"),
                    TraceEvent("AdaptiveSelect", 3, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [2, 2]"),
                    TraceEvent("AdaptiveT1", 7, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [23]"),
                    TraceEvent("AdaptiveT1", 7, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [21]"),
                    TraceEvent("AdaptiveT1", 7, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [21]"),
                    TraceEvent("AdaptiveT1", 7, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [21]"),
                    TraceEvent("AdaptiveSelect", 3, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [2, 2]"),
                    TraceEvent("AdaptiveSelectTest", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [1]"),
                    TraceEvent("AdaptiveSelectTest", 2, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [0]"),
                    TraceEvent("AdaptiveSelect", 3, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [2, 2]"),
                    TraceEvent("AdaptiveSelect", 3, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000003 state: [-1, 2]"),
                    TraceEvent("AdaptiveSelect", 3, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000003 state: [-1, 2]"),
                    TraceEvent("AdaptiveT1", 7, "before-Unmount"),
                    TraceEvent("AdaptiveT1", 7, "after-Unmount"),
                    TraceEvent("AdaptiveT1", 7, "before-Dispose", ""),
                    TraceEvent("AdaptiveT1", 7, "after-Dispose", ""),
                    TraceEvent("AdaptiveSelect", 3, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [-1, -1]"),
                    TraceEvent("AdaptiveSelectTest", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [0]")
                )
            ),
            adapter.actual(dumpCode = false)
        )
    }


    @Test
    fun testWithNonEmptyStart() {
        val adapter = AdaptiveTestAdapter()

        AdaptiveSelectTest(adapter, null, 0).apply {
            state[0] = 1
            create()
            mount()
        }

        assertEquals(
            adapter.expected(
                listOf(
                    TraceEvent("AdaptiveSelectTest", 2, "before-Create", ""),
                    TraceEvent("AdaptiveSelectTest", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [1]"),
                    TraceEvent("AdaptiveSelectTest", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [1]"),
                    TraceEvent("AdaptiveSelectTest", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [1]"),
                    TraceEvent("AdaptiveSelectTest", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [1]"),
                    TraceEvent("AdaptiveSelect", 3, "before-Create", ""),
                    TraceEvent("AdaptiveSelect", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, -1]"),
                    TraceEvent("AdaptiveSelect", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [2, -1]"),
                    TraceEvent("AdaptiveSelect", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [2, -1]"),
                    TraceEvent("AdaptiveT1", 5, "before-Create", ""),
                    TraceEvent("AdaptiveT1", 5, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveT1", 5, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [21]"),
                    TraceEvent("AdaptiveT1", 5, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [21]"),
                    TraceEvent("AdaptiveT1", 5, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [21]"),
                    TraceEvent("AdaptiveT1", 5, "after-Create", ""),
                    TraceEvent("AdaptiveSelect", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [2, 2]"),
                    TraceEvent("AdaptiveSelect", 3, "after-Create", ""),
                    TraceEvent("AdaptiveSelectTest", 2, "after-Create", ""),
                    TraceEvent("AdaptiveSelectTest", 2, "before-Mount"),
                    TraceEvent("AdaptiveSelect", 3, "before-Mount"),
                    TraceEvent("TestPlaceholder", 4, "before-Mount"),
                    TraceEvent("TestPlaceholder", 4, "after-Mount"),
                    TraceEvent("AdaptiveT1", 5, "before-Mount"),
                    TraceEvent("AdaptiveT1", 5, "after-Mount"),
                    TraceEvent("AdaptiveSelect", 3, "after-Mount"),
                    TraceEvent("AdaptiveSelectTest", 2, "after-Mount")
                )
            ),
            adapter.actual(dumpCode = false)
        )
    }

}

class AdaptiveSelectTest(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int
) : AdaptiveFragment(adapter, parent, index, 1) {

    var v0: Int
        get() = state[0] as Int
        set(v) {
            setStateVariable(0, v)
        }

    val dependencyMask_0_0 = 0x01 // fragment index: 0, state variable index: 0
    val dependencyMask_1_0 = 0x01 // fragment index: 1, state variable index: 0
    val dependencyMask_2_0 = 0x01 // fragment index: 2, state variable index: 0

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment {
        val fragment = when (declarationIndex) {
            0 -> AdaptiveSelect(adapter, parent, declarationIndex)
            1 -> AdaptiveT1(adapter, parent, declarationIndex)
            2 -> AdaptiveT1(adapter, parent, declarationIndex)
            else -> invalidIndex(declarationIndex) // throws exception
        }

        fragment.create()

        return fragment
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {

        val closureMask = fragment.getCreateClosureDirtyMask()

        when (fragment.index) {
            0 -> {
                if (fragment.haveToPatch(closureMask, dependencyMask_0_0)) {
                    fragment.setStateVariable(
                        0,
                        when {
                            v0 == 0 -> - 1
                            v0 % 2 == 0 -> 1
                            else -> 2
                        }
                    )
                }
            }

            1 -> {
                if (fragment.haveToPatch(closureMask, dependencyMask_1_0)) {
                    fragment.setStateVariable(
                        0,
                        v0 + 10
                    )
                }
            }

            2 -> {
                if (fragment.haveToPatch(closureMask, dependencyMask_2_0)) {
                    fragment.setStateVariable(
                        0,
                        v0 + 20
                    )
                }
            }
        }
    }

    override fun genPatchInternal() {

    }

}