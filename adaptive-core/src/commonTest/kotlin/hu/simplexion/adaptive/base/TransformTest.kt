/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.base

import hu.simplexion.adaptive.base.testing.AdaptiveTestAdapter
import hu.simplexion.adaptive.base.testing.AdaptiveTestBridge
import hu.simplexion.adaptive.base.testing.TestNode
import hu.simplexion.adaptive.base.testing.TraceEvent
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("unused")
fun Adaptive.transformTest() {
    transformed() transform 23
}

fun Adaptive.transformed(@Suppress("UNUSED_PARAMETER") i : Int = 12): TestState {
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
        val root = AdaptiveTestBridge(1)

        AdaptiveTransformTest(adapter, null, 0).apply {
            create()
            mount(root)
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
                    TraceEvent("AdaptiveTransformTest", 2, "before-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveTransformed", 3, "before-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveTransformed", 3, "after-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveTransformTest", 2, "after-Mount", "bridge: 1")
                    //@formatter:on
                )
            ),
            adapter.actual(dumpCode = false)
        )
    }
}

class AdaptiveTransformTest(
    adapter: AdaptiveAdapter<TestNode>,
    parent: AdaptiveFragment<TestNode>?,
    index: Int
) : AdaptiveFragment<TestNode>(adapter, parent, index, 0) {

    val dependencyMask_0_0 = 0x00 // fragment index: 0, state variable index: 0

    override fun genBuild(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode> {

        val fragment = when (declarationIndex) {
            0 -> AdaptiveTransformed(adapter, parent, declarationIndex)
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
                    (fragment as TestState).transform(23)
                }
            }
        }
    }

    override fun genPatchInternal() {

    }

}

class AdaptiveTransformed(
    adapter: AdaptiveAdapter<TestNode>,
    parent: AdaptiveFragment<TestNode>?,
    index: Int
) : AdaptiveFragment<TestNode>(adapter, parent, index, 1), TestState {

    val dependencyMask_0_0 = 0x00 // fragment index: 0, state variable index: 0

    override fun genBuild(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode>? {
        return null
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment<TestNode>) {

    }

    override fun genPatchInternal() {
        // here should be the internal patch for the default, but it is not important for this test
    }

}