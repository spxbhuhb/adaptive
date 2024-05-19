/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.foundation

import hu.simplexion.adaptive.foundation.internal.BoundSupportFunction
import hu.simplexion.adaptive.foundation.testing.AdaptiveTestAdapter
import hu.simplexion.adaptive.foundation.testing.AdaptiveTestFragment
import hu.simplexion.adaptive.foundation.testing.TraceEvent
import kotlin.test.Test
import kotlin.test.assertEquals

@Adaptive
@Suppress("unused")
fun supportFunctionTest() {
    var i = 13
    supportFunctionInner(12) { i += 11 + it }
}

@Adaptive
fun supportFunctionInner(i: Int, supportFun: (i: Int) -> Unit) {
    supportFun(i)
}

class SupportFunctionTest {

    @Test
    fun test() {
        val adapter = AdaptiveTestAdapter()

        AdaptiveSupportFunctionTest(adapter, null, 0).apply {
            create()
            mount()
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
                    TraceEvent("AdaptiveSupportFunctionInner", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12, BoundSupportFunction(2, 3, 0)]"),
                    TraceEvent("AdaptiveSupportFunctionInner", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12, BoundSupportFunction(2, 3, 0)]"),
                    TraceEvent("AdaptiveSupportFunctionTest", 2, "before-Invoke", "BoundSupportFunction(2, 3, 0) arguments: [12]"),
                    TraceEvent("AdaptiveSupportFunctionTest", 2, "after-Invoke", "index: 0 result: kotlin.Unit"),
                    TraceEvent("AdaptiveSupportFunctionInner", 3, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [12, BoundSupportFunction(2, 3, 0)]"),
                    TraceEvent("AdaptiveSupportFunctionInner", 3, "after-Create", ""),
                    TraceEvent("AdaptiveSupportFunctionTest", 2, "after-Create", ""),
                    TraceEvent("AdaptiveSupportFunctionTest", 2, "before-Mount"),
                    TraceEvent("AdaptiveSupportFunctionInner", 3, "before-Mount"),
                    TraceEvent("AdaptiveSupportFunctionInner", 3, "after-Mount"),
                    TraceEvent("AdaptiveSupportFunctionTest", 2, "after-Mount")
                )
            ),
            adapter.actual(dumpCode = false)
        )
    }
}

class AdaptiveSupportFunctionTest(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int
) : AdaptiveTestFragment(adapter, parent, index, 1) {

    val dependencyMask_0_0 = 0x00 // fragment index: 0, state variable index: 0
    val dependencyMask_0_1 = 0x00 // fragment index: 1, state variable index: 1

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment {

        val fragment = when (declarationIndex) {
            0 -> AdaptiveSupportFunctionInner(adapter, parent, declarationIndex)
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
                    fragment.setStateVariable(0, 12)
                }
                if (fragment.haveToPatch(closureMask, dependencyMask_0_1)) {
                    fragment.setStateVariable(1, BoundSupportFunction(this, fragment, 0))
                }
            }
        }
    }

    override fun genPatchInternal(): Boolean {
        val closureMask = getThisClosureDirtyMask()

        if (haveToPatch(closureMask, 0)) {
            setStateVariable(0, 13)
        }

        return true
    }

    @Suppress("RedundantNullableReturnType")
    override fun genInvoke(
        supportFunction: BoundSupportFunction,
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
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int
) : AdaptiveTestFragment(adapter, parent, index, 2) {

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? = null

    override fun genPatchInternal(): Boolean {
        (getThisClosureVariable(1) as BoundSupportFunction).invoke(getThisClosureVariable(0))
        return true
    }

}