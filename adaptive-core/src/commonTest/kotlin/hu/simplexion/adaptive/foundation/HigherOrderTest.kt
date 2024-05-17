/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.foundation

import hu.simplexion.adaptive.foundation.internal.BoundFragmentFactory
import hu.simplexion.adaptive.foundation.structural.AdaptiveAnonymous
import hu.simplexion.adaptive.foundation.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * ```kotlin
 * fun Adaptive.higherOrderTest() {
 *     higherFun(12) { lowerFunI1 ->
 *         higherFun(lowerFunI1) { lowerFunI2 ->
 *             T1(lowerFunI1 + lowerFunI2)
 *         }
 *     }
 * }
 *
 * fun Adaptive.higherFun(higherI : Int, lowerFun : Adaptive.(lowerFunI : Int) -> Unit) {
 *     higherFunInner(higherI*2) { lowerFunInnerI ->
 *         lowerFun(higherI + lowerFunInnerI)
 *     }
 * }
 *
 * fun Adaptive.higherFunInner(innerI : Int, lowerFunInner : Adaptive.(lowerFunInnerI : Int) -> Unit) {
 *     lowerFunInner(innerI + 1) // Anonymous 1, Anonymous 3
 * }
 * ```
 */
class HigherOrderTest {

    @Test
    fun test() {
        val adapter = AdaptiveTestAdapter()

        AdaptiveHigherOrderTest(adapter, null, 0).apply {
            create()
            mount()
        }

        assertEquals(
            adapter.expected(
                listOf(
                    TraceEvent("AdaptiveHigherOrderTest", 2, "before-Create", ""),
                    TraceEvent("AdaptiveHigherOrderTest", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
                    TraceEvent("AdaptiveHigherOrderTest", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
                    TraceEvent("AdaptiveHigherOrderTest", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
                    TraceEvent("AdaptiveHigherOrderTest", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
                    TraceEvent("AdaptiveHigherFun", 3, "before-Create", ""),
                    TraceEvent("AdaptiveHigherFun", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
                    TraceEvent("AdaptiveHigherFun", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12, BoundFragmentFactory(2,1)]"),
                    TraceEvent("AdaptiveHigherFun", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12, BoundFragmentFactory(2,1)]"),
                    TraceEvent("AdaptiveHigherFun", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12, BoundFragmentFactory(2,1)]"),
                    TraceEvent("AdaptiveHigherFunInner", 4, "before-Create", ""),
                    TraceEvent("AdaptiveHigherFunInner", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
                    TraceEvent("AdaptiveHigherFunInner", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [24, BoundFragmentFactory(3,1)]"),
                    TraceEvent("AdaptiveHigherFunInner", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [24, BoundFragmentFactory(3,1)]"),
                    TraceEvent("AdaptiveHigherFunInner", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [24, BoundFragmentFactory(3,1)]"),
                    TraceEvent("AdaptiveAnonymous", 5, "before-Create", ""),
                    TraceEvent("AdaptiveAnonymous", 5, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveAnonymous", 5, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [25]"),
                    TraceEvent("AdaptiveAnonymous", 5, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [25]"),
                    TraceEvent("AdaptiveAnonymous", 5, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [25]"),
                    TraceEvent("AdaptiveAnonymous", 6, "before-Create", ""),
                    TraceEvent("AdaptiveAnonymous", 6, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveAnonymous", 6, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [37]"),
                    TraceEvent("AdaptiveAnonymous", 6, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [37]"),
                    TraceEvent("AdaptiveAnonymous", 6, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [37]"),
                    TraceEvent("AdaptiveHigherFun", 7, "before-Create", ""),
                    TraceEvent("AdaptiveHigherFun", 7, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
                    TraceEvent("AdaptiveHigherFun", 7, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [37, BoundFragmentFactory(2,2)]"),
                    TraceEvent("AdaptiveHigherFun", 7, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [37, BoundFragmentFactory(2,2)]"),
                    TraceEvent("AdaptiveHigherFun", 7, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [37, BoundFragmentFactory(2,2)]"),
                    TraceEvent("AdaptiveHigherFunInner", 8, "before-Create", ""),
                    TraceEvent("AdaptiveHigherFunInner", 8, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
                    TraceEvent("AdaptiveHigherFunInner", 8, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [74, BoundFragmentFactory(7,1)]"),
                    TraceEvent("AdaptiveHigherFunInner", 8, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [74, BoundFragmentFactory(7,1)]"),
                    TraceEvent("AdaptiveHigherFunInner", 8, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [74, BoundFragmentFactory(7,1)]"),
                    TraceEvent("AdaptiveAnonymous", 9, "before-Create", ""),
                    TraceEvent("AdaptiveAnonymous", 9, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveAnonymous", 9, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [75]"),
                    TraceEvent("AdaptiveAnonymous", 9, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [75]"),
                    TraceEvent("AdaptiveAnonymous", 9, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [75]"),
                    TraceEvent("AdaptiveAnonymous", 10, "before-Create", ""),
                    TraceEvent("AdaptiveAnonymous", 10, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveAnonymous", 10, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [112]"),
                    TraceEvent("AdaptiveAnonymous", 10, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [112]"),
                    TraceEvent("AdaptiveAnonymous", 10, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [112]"),
                    TraceEvent("AdaptiveT1", 11, "before-Create", ""),
                    TraceEvent("AdaptiveT1", 11, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveT1", 11, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [149]"),
                    TraceEvent("AdaptiveT1", 11, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [149]"),
                    TraceEvent("AdaptiveT1", 11, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [149]"),
                    TraceEvent("AdaptiveT1", 11, "after-Create", ""),
                    TraceEvent("AdaptiveAnonymous", 10, "after-Create", ""),
                    TraceEvent("AdaptiveAnonymous", 9, "after-Create", ""),
                    TraceEvent("AdaptiveHigherFunInner", 8, "after-Create", ""),
                    TraceEvent("AdaptiveHigherFun", 7, "after-Create", ""),
                    TraceEvent("AdaptiveAnonymous", 6, "after-Create", ""),
                    TraceEvent("AdaptiveAnonymous", 5, "after-Create", ""),
                    TraceEvent("AdaptiveHigherFunInner", 4, "after-Create", ""),
                    TraceEvent("AdaptiveHigherFun", 3, "after-Create", ""),
                    TraceEvent("AdaptiveHigherOrderTest", 2, "after-Create", ""),
                    TraceEvent("AdaptiveHigherOrderTest", 2, "before-Mount"),
                    TraceEvent("AdaptiveHigherFun", 3, "before-Mount"),
                    TraceEvent("AdaptiveHigherFunInner", 4, "before-Mount"),
                    TraceEvent("AdaptiveAnonymous", 5, "before-Mount"),
                    TraceEvent("AdaptiveAnonymous", 6, "before-Mount"),
                    TraceEvent("AdaptiveHigherFun", 7, "before-Mount"),
                    TraceEvent("AdaptiveHigherFunInner", 8, "before-Mount"),
                    TraceEvent("AdaptiveAnonymous", 9, "before-Mount"),
                    TraceEvent("AdaptiveAnonymous", 10, "before-Mount"),
                    TraceEvent("AdaptiveT1", 11, "before-Mount"),
                    TraceEvent("AdaptiveT1", 11, "after-Mount"),
                    TraceEvent("AdaptiveAnonymous", 10, "after-Mount"),
                    TraceEvent("AdaptiveAnonymous", 9, "after-Mount"),
                    TraceEvent("AdaptiveHigherFunInner", 8, "after-Mount"),
                    TraceEvent("AdaptiveHigherFun", 7, "after-Mount"),
                    TraceEvent("AdaptiveAnonymous", 6, "after-Mount"),
                    TraceEvent("AdaptiveAnonymous", 5, "after-Mount"),
                    TraceEvent("AdaptiveHigherFunInner", 4, "after-Mount"),
                    TraceEvent("AdaptiveHigherFun", 3, "after-Mount"),
                    TraceEvent("AdaptiveHigherOrderTest", 2, "after-Mount")
                )
            ),
            adapter.actual(dumpCode = false)
        )
    }
}

class AdaptiveHigherOrderTest(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int
) : AdaptiveFragment(adapter, parent, index, 0) {

    val dependencyMask_0_0 = 0x00 // fragment index: 0, state variable index: 0
    val dependencyMask_0_1 = 0x00 // fragment index: 0, state variable index: 1

    val dependencyMask_1_0 = 0x00 // fragment index: 1, state variable index: 0
    val dependencyMask_1_1 = 0x00 // fragment index: 1, state variable index: 1

    val dependencyMask_2_0 = 0x00 // fragment index: 2, state variable index: 0

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment {
        val fragment = when (declarationIndex) {
            0 -> AdaptiveHigherFun(adapter, parent, declarationIndex)
            1 -> AdaptiveHigherFun(adapter, parent, declarationIndex)
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
                    fragment.setStateVariable(0, 12)
                }
                if (fragment.haveToPatch(closureMask, dependencyMask_0_1)) {
                    fragment.setStateVariable(1, BoundFragmentFactory(this, 1))
                }
            }

            1 -> {
                if (fragment.haveToPatch(closureMask, dependencyMask_1_0)) {
                    fragment.setStateVariable(0, fragment.getCreateClosureVariable(0) as Int)
                }
                if (fragment.haveToPatch(closureMask, dependencyMask_1_1)) {
                    fragment.setStateVariable(1, BoundFragmentFactory(this, 2))
                }
            }

            2 -> {
                if (fragment.haveToPatch(closureMask, dependencyMask_2_0)) {
                    fragment.setStateVariable(0, (fragment.getCreateClosureVariable(0) as Int) + (fragment.getCreateClosureVariable(1) as Int))
                }
            }

            else -> invalidIndex(fragment.index)
        }
    }

    override fun genPatchInternal() {

    }
}

class AdaptiveHigherFun(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int
) : AdaptiveFragment(adapter, parent, index, 2) {

    val higherI
        get() = state[0] as Int

    @Suppress("UNCHECKED_CAST")
    val builder
        get() = state[1] as BoundFragmentFactory

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment {
        val fragment = when (declarationIndex) {
            0 -> AdaptiveHigherFunInner(adapter, parent, declarationIndex)
            1 -> AdaptiveAnonymous(adapter, parent, declarationIndex, 1, builder)
            else -> invalidIndex(declarationIndex) // throws exception
        }

        fragment.create()

        return fragment
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {
        when (fragment.index) {
            0 -> {
                // TODO haveToPatch
                fragment.setStateVariable(0, higherI * 2)
                fragment.setStateVariable(1, BoundFragmentFactory(this, 1))
            }

            1 -> {
                // TODO haveToPatch
                // higherI + lowerFunInnerI
                fragment.setStateVariable(0, (fragment.getCreateClosureVariable(0) as Int) + (fragment.getCreateClosureVariable(2) as Int))
            }

            else -> invalidIndex(fragment.index)
        }
    }

    override fun genPatchInternal() {

    }

}

class AdaptiveHigherFunInner(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int
) : AdaptiveFragment(adapter, parent, index, 2) {

    val innerI
        get() = state[0] as Int

    @Suppress("UNCHECKED_CAST")
    val builder
        get() = state[1] as BoundFragmentFactory

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment {
        val fragment = when (declarationIndex) {
            0 -> AdaptiveAnonymous(adapter, parent, declarationIndex, 1, builder)
            else -> invalidIndex(declarationIndex) // throws exception
        }

        fragment.create()

        return fragment
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {
        when (fragment.index) {
            0 -> {
                fragment.state[0] = innerI + 1
            }

            else -> invalidIndex(fragment.index)
        }
    }

    override fun genPatchInternal() {

    }

}