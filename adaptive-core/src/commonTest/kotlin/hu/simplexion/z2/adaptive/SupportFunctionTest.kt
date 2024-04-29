/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

import hu.simplexion.z2.adaptive.structural.AdaptivePlaceholder
import hu.simplexion.z2.adaptive.testing.AdaptiveTestAdapter
import hu.simplexion.z2.adaptive.testing.AdaptiveTestBridge
import hu.simplexion.z2.adaptive.testing.TestNode
import hu.simplexion.z2.adaptive.testing.TraceEvent
import kotlin.test.Test
import kotlin.test.assertEquals


@Suppress("unused")
fun Adaptive.supportFunctionTest() {
    var i = 13
    supportFunctionInner(12) { i += 11 + it }
}

@Suppress("UnusedReceiverParameter")
fun Adaptive.supportFunctionInner(i: Int, supportFun: (i: Int) -> Unit) {
    supportFun(i)
}

class SupportFunctionTest {

    @Test
    fun test() {
        val adapter = AdaptiveTestAdapter()
        val root = AdaptiveTestBridge(1)

        AdaptiveSupportFunctionTest(adapter, null, 0).apply {
            create()
            mount(root)
        }.also {
            assertEquals(11 + 12 + 13, it.state[0])
        }

        assertEquals(
            adapter.expected(
                listOf(
                    TraceEvent("AdaptiveSupportFunctionTest", 2, "before-Create", ""),
                    TraceEvent("AdaptiveSupportFunctionTest", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveSupportFunctionTest", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveSupportFunctionTest", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveSupportFunctionTest", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [13]"),
                    TraceEvent("AdaptiveSupportFunctionInner", 3, "before-Create", ""),
                    TraceEvent("AdaptiveSupportFunctionInner", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
                    TraceEvent("AdaptiveSupportFunctionInner", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12, AdaptiveSupportFunction(2, 3, 0)]"),
                    TraceEvent("AdaptiveSupportFunctionInner", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12, AdaptiveSupportFunction(2, 3, 0)]"),
                    TraceEvent("AdaptiveSupportFunctionTest", 2, "before-Invoke", "AdaptiveSupportFunction(2, 3, 0) arguments: [12]"),
                    TraceEvent("AdaptiveSupportFunctionTest", 2, "after-Invoke", "index: 0 result: kotlin.Unit"),
                    TraceEvent("AdaptiveSupportFunctionInner", 3, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [12, AdaptiveSupportFunction(2, 3, 0)]"),
                    TraceEvent("AdaptiveSupportFunctionInner", 3, "after-Create", ""),
                    TraceEvent("AdaptiveSupportFunctionTest", 2, "after-Create", ""),
                    TraceEvent("AdaptiveSupportFunctionTest", 2, "before-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveSupportFunctionInner", 3, "before-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveSupportFunctionInner", 3, "after-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveSupportFunctionTest", 2, "after-Mount", "bridge: 1")
                )
            ),
            adapter.actual(dumpCode = false)
        )
    }
}

class AdaptiveSupportFunctionTest(
    adapter: AdaptiveAdapter<TestNode>,
    parent: AdaptiveFragment<TestNode>?,
    index: Int
) : AdaptiveFragment<TestNode>(adapter, parent, index, 1) {

    val dependencyMask_0_0 = 0x00 // fragment index: 0, state variable index: 0
    val dependencyMask_0_1 = 0x00 // fragment index: 1, state variable index: 1

    override fun genBuild(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode> {

        val fragment = when (declarationIndex) {
            0 -> AdaptiveSupportFunctionInner(adapter, parent, declarationIndex)
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
                    fragment.setStateVariable(0, 12)
                }
                if (fragment.haveToPatch(closureMask, dependencyMask_0_1)) {
                    fragment.setStateVariable(1, AdaptiveSupportFunction(this, fragment, 0))
                }
            }
        }
    }

    override fun genPatchInternal() {
        val closureMask = getThisClosureDirtyMask()

        if (haveToPatch(closureMask, 0)) {
            setStateVariable(0, 13)
        }
    }

    @Suppress("RedundantNullableReturnType")
    override fun genInvoke(
        supportFunction: AdaptiveSupportFunction,
        arguments: Array<out Any?>
    ): Any? {

        val declaringFragment = supportFunction.declaringFragment
        val receivingFragment = supportFunction.receivingFragment

        return when (supportFunction.supportFunctionIndex) {
            0 -> {
                declaringFragment.setStateVariable(
                    0,
                    11 + (arguments[0] as Int) + (receivingFragment.getCreateClosureVariable(0) as Int)
                )
            }

            else -> invalidIndex(supportFunction.supportFunctionIndex)
        }
    }
}

class AdaptiveSupportFunctionInner(
    adapter: AdaptiveAdapter<TestNode>,
    parent: AdaptiveFragment<TestNode>?,
    index: Int
) : AdaptiveFragment<TestNode>(adapter, parent, index, 2) {

    override fun genBuild(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode> {
        return AdaptivePlaceholder(adapter, this, - 1)
    }

    override fun genPatchInternal() {
        (getThisClosureVariable(1) as AdaptiveSupportFunction).invoke(getThisClosureVariable(0))
    }

}