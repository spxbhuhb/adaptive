/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.foundation

import hu.simplexion.adaptive.foundation.testing.AdaptiveTestAdapter
import hu.simplexion.adaptive.foundation.testing.AdaptiveTestFragment
import hu.simplexion.adaptive.foundation.testing.TraceEvent
import kotlin.test.Test
import kotlin.test.assertEquals

@Adaptive
@Suppress("unused")
fun transformTest() {
    transformed() transform 23
}

@Adaptive
fun transformed(@Suppress("UNUSED_PARAMETER") i : Int = 12): TestState {
    return thisState()
}

interface TestState: AdaptiveTransformInterface {
    infix fun transform(i : Int) {
        setStateVariable(0, i)
    }
}

class TransformTest {

    @Test
    fun test() {
        val adapter = AdaptiveTestAdapter()

        AdaptiveTransformTest(adapter, null, 0).apply {
            create()
            mount()
        }

        assertEquals(
            adapter.expected(
                listOf(
                    //@formatter:off
                    TraceEvent("AdaptiveTransformTest", 2, "before-Create", ""),
                    TraceEvent("AdaptiveTransformTest", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
                    TraceEvent("AdaptiveTransformTest", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
                    TraceEvent("AdaptiveTransformTest", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
                    TraceEvent("AdaptiveTransformTest", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
                    TraceEvent("AdaptiveTransformed", 3, "before-Create", ""),
                    TraceEvent("AdaptiveTransformed", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveTransformed", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [23]"),
                    TraceEvent("AdaptiveTransformed", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [23]"),
                    TraceEvent("AdaptiveTransformed", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [23]"),
                    TraceEvent("AdaptiveTransformed", 3, "after-Create", ""),
                    TraceEvent("AdaptiveTransformTest", 2, "after-Create", ""),
                    TraceEvent("AdaptiveTransformTest", 2, "before-Mount"),
                    TraceEvent("AdaptiveTransformed", 3, "before-Mount"),
                    TraceEvent("AdaptiveTransformed", 3, "after-Mount"),
                    TraceEvent("AdaptiveTransformTest", 2, "after-Mount")
                    //@formatter:on
                )
            ),
            adapter.actual(dumpCode = false)
        )
    }
}

class AdaptiveTransformTest(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int
) : AdaptiveTestFragment(adapter, parent, index, 0) {

    val dependencyMask_0_0 = 0x00 // fragment index: 0, state variable index: 0

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment {

        val fragment = when (declarationIndex) {
            0 -> AdaptiveTransformed(adapter, parent, declarationIndex)
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
                    (fragment as TestState).transform(23)
                }
            }
        }
    }

    override fun genPatchInternal(): Boolean = true

}

class AdaptiveTransformed(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int
) : AdaptiveFragment(adapter, parent, index, -1, 1), TestState {

    val dependencyMask_0_0 = 0x00 // fragment index: 0, state variable index: 0

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? {
        return null
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {

    }

    override fun genPatchInternal(): Boolean {
        // here should be the internal patch for the default, but it is not important for this test
        return true
    }

}