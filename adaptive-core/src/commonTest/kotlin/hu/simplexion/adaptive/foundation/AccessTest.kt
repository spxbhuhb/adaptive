/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.foundation

import hu.simplexion.adaptive.foundation.binding.AdaptiveStateVariableBinding
import hu.simplexion.adaptive.foundation.testing.*
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
    @Suppress("UNUSED_PARAMETER") selector: () -> T
) {
    checkNotNull(binding)
    T1(binding.value as Int)
}

class AccessTest {

    @Test
    fun test() {
        val adapter = AdaptiveTestAdapter()

        AdaptiveAccessBindingTest(adapter, null, 0).apply {
            create()
            mount()
        }

        assertEquals(
            adapter.expected(
                listOf(
                    //@formatter:off
                    TraceEvent("AdaptiveAccessBindingTest", 2, "before-Create", ""),
                    TraceEvent("AdaptiveAccessBindingTest", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveAccessBindingTest", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveAccessBindingTest", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveAccessBindingTest", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
                    TraceEvent("AdaptiveAccessor", 3, "before-Create", ""),
                    TraceEvent("AdaptiveAccessor", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveAccessBindingTest", 2, "before-Add-Binding", "binding: AdaptiveStateVariableBinding(2, 0, 0, 3, 0, null, -1, AdaptivePropertyMetadata(kotlin.Int))"),
                    TraceEvent("AdaptiveAccessBindingTest", 2, "after-Add-Binding", "binding: AdaptiveStateVariableBinding(2, 0, 0, 3, 0, null, -1, AdaptivePropertyMetadata(kotlin.Int))"),
                    TraceEvent("AdaptiveAccessor", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [AdaptiveStateVariableBinding(2, 0, 0, 3, 0, null, -1, AdaptivePropertyMetadata(kotlin.Int))]"),
                    TraceEvent("AdaptiveAccessor", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [AdaptiveStateVariableBinding(2, 0, 0, 3, 0, null, -1, AdaptivePropertyMetadata(kotlin.Int))]"),
                    TraceEvent("AdaptiveAccessor", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [AdaptiveStateVariableBinding(2, 0, 0, 3, 0, null, -1, AdaptivePropertyMetadata(kotlin.Int))]"),
                    TraceEvent("AdaptiveT1", 4, "before-Create", ""),
                    TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
                    TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
                    TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
                    TraceEvent("AdaptiveT1", 4, "after-Create", ""),
                    TraceEvent("AdaptiveAccessor", 3, "after-Create", ""),
                    TraceEvent("AdaptiveAccessBindingTest", 2, "after-Create", ""),
                    TraceEvent("AdaptiveAccessBindingTest", 2, "before-Mount"),
                    TraceEvent("AdaptiveAccessor", 3, "before-Mount"),
                    TraceEvent("AdaptiveT1", 4, "before-Mount"),
                    TraceEvent("AdaptiveT1", 4, "after-Mount"),
                    TraceEvent("AdaptiveAccessor", 3, "after-Mount"),
                    TraceEvent("AdaptiveAccessBindingTest", 2, "after-Mount")
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
) : AdaptiveTestFragment(adapter, parent, index, 1) {

    val dependencyMask_0_0 = 0x01 // fragment index: 0, state variable index: 0

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment {

        val fragment = when (declarationIndex) {
            0 -> AdaptiveAccessor(adapter, parent, declarationIndex)
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
                    setBinding(0, fragment, 0, null, "kotlin.Int")
                }
            }
        }
    }

    override fun genPatchInternal() {
        state[0] = 12
    }

}

class AdaptiveAccessor(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int
) : AdaptiveFragment(adapter, parent, index, 1) {

    val dependencyMask_0_0 = 0x00 // fragment index: 0, state variable index: 0

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

        when (fragment.index) {
            0 -> {
                if (fragment.haveToPatch(closureMask, dependencyMask_0_0)) {
                    @Suppress("UNCHECKED_CAST")
                    fragment.setStateVariable(0, (getThisClosureVariable(0) as AdaptiveStateVariableBinding<Int>).value)
                }
            }
        }
    }

    override fun genPatchInternal() {

    }

}