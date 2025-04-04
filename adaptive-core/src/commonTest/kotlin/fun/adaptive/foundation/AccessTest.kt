/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.foundation

import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("unused")
@Adaptive
fun accessTest() {
    val a = 12
    accessor { a }
}

@Adaptive
fun <T> accessor(
    binding: AdaptiveStateVariableBinding<T>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> T
) {
    checkNotNull(binding)
    T1(binding.value as Int)
}

class AccessTest {

    @Test
    fun test() {
        val adapter = AdaptiveTestAdapter()

        AdaptiveAccessBindingTest(adapter, null, 1).apply {
            create()
            mount()
        }

        assertEquals(
            adapter.expected(
                listOf(
                    //@formatter:off
                    TraceEvent("AdaptiveAccessBindingTest", 2, "before-Create", ""),
                    TraceEvent("AdaptiveAccessBindingTest", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
                    TraceEvent("AdaptiveAccessBindingTest", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
                    TraceEvent("AdaptiveAccessBindingTest", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
                    TraceEvent("AdaptiveAccessBindingTest", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 12]"),
                    TraceEvent("AdaptiveAccessor", 3, "before-Create", ""),
                    TraceEvent("AdaptiveAccessor", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
                    TraceEvent("AdaptiveAccessBindingTest", 2, "before-Add-Binding", "binding: AdaptiveStateVariableBinding(2, 1, 1, 3, 1, null, kotlin.Int, null)"),
                    TraceEvent("AdaptiveAccessBindingTest", 2, "after-Add-Binding", "binding: AdaptiveStateVariableBinding(2, 1, 1, 3, 1, null, kotlin.Int, null)"),
                    TraceEvent("AdaptiveAccessor", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, AdaptiveStateVariableBinding(2, 1, 1, 3, 1, null, kotlin.Int, null)]"),
                    TraceEvent("AdaptiveAccessor", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, AdaptiveStateVariableBinding(2, 1, 1, 3, 1, null, kotlin.Int, null)]"),
                    TraceEvent("AdaptiveAccessor", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, AdaptiveStateVariableBinding(2, 1, 1, 3, 1, null, kotlin.Int, null)]"),
                    TraceEvent("AdaptiveT1", 4, "before-Create", ""),
                    TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
                    TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12]"),
                    TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12]"),
                    TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 12]"),
                    TraceEvent("AdaptiveT1", 4, "after-Create", ""),
                    TraceEvent("AdaptiveAccessor", 3, "after-Create", ""),
                    TraceEvent("AdaptiveAccessBindingTest", 2, "after-Create", ""),
                    TraceEvent("AdaptiveAccessBindingTest", 2, "before-Mount", ""),
                    TraceEvent("AdaptiveAccessor", 3, "before-Mount", ""),
                    TraceEvent("AdaptiveT1", 4, "before-Mount", ""),
                    TraceEvent("AdaptiveT1", 4, "after-Mount", ""),
                    TraceEvent("AdaptiveAccessor", 3, "after-Mount", ""),
                    TraceEvent("AdaptiveAccessBindingTest", 2, "after-Mount", "")
                    //@formatter:on
                )
            ),
            adapter.actual(dumpCode = false)
        )
    }
}

class AdaptiveAccessBindingTest(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int
) : AdaptiveTestFragment(adapter, parent, index, 2) {

    val dependencyMask_0_1 = 0x02 // fragment index: 0, state variable index: 1

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment {

        val fragment = when (declarationIndex) {
            0 -> AdaptiveAccessor(parent.adapter, parent, declarationIndex)
            else -> invalidIndex(declarationIndex) // throws exception
        }

        fragment.create()

        return fragment
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {

        val closureMask = fragment.getCreateClosureDirtyMask()

        when (fragment.declarationIndex) {
            0 -> {
                if (fragment.haveToPatch(closureMask, dependencyMask_0_1)) {
                    setBinding(1, fragment, 1, null, "kotlin.Int", null)
                }
            }
        }
    }

    override fun genPatchInternal(): Boolean {
        state[1] = 12
        return true
    }

}

class AdaptiveAccessor(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int
) : AdaptiveFragment(adapter, parent, index, 2) {

    val dependencyMask_0_1 = 0x02 // fragment index: 0, state variable index: 1

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment {

        val fragment = when (declarationIndex) {
            0 -> AdaptiveT1(parent.adapter, parent, declarationIndex)
            else -> invalidIndex(declarationIndex) // throws exception
        }

        fragment.create()

        return fragment
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {

        val closureMask = fragment.getCreateClosureDirtyMask()

        when (fragment.declarationIndex) {
            0 -> {
                if (fragment.haveToPatch(closureMask, dependencyMask_0_1)) {
                    @Suppress("UNCHECKED_CAST")
                    fragment.setStateVariable(1, (getThisClosureVariable(1) as AdaptiveStateVariableBinding<Int>).value)
                }
            }
        }
    }

    override fun genPatchInternal(): Boolean = true

}