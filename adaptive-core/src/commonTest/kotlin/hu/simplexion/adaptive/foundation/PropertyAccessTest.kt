/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.foundation

import hu.simplexion.adaptive.foundation.binding.AdaptivePropertyProvider
import hu.simplexion.adaptive.foundation.binding.AdaptiveStateVariableBinding
import hu.simplexion.adaptive.foundation.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class TestProvider : AdaptivePropertyProvider {

    val values = arrayOf<Any?>(12)

    var i : Int
        get() = values[0] as Int
        set(v) {
            values[0] = v
            bindings.forEach { it.setValue(v, false) }
        }

    val bindings = mutableListOf<AdaptiveStateVariableBinding<*>>()

    override fun addBinding(binding: AdaptiveStateVariableBinding<*>) {
        bindings += binding
    }

    override fun removeBinding(binding: AdaptiveStateVariableBinding<*>) {
        bindings += binding
    }

    override fun getValue(path: Array<String>): Any? {
        return when {
            path[0] == "i" -> values[0]
            else -> throw AssertionError("unknown property, path=$path")
        }
    }

    override fun setValue(path: Array<String>, value: Any?) {
        when {
            path[0] == "i" -> values[0] = value
            else -> throw AssertionError("unknown property, path=$path")
        }
    }

    override fun toString(): String {
        return "TestProvider()"
    }
}

lateinit var testBinding : AdaptiveStateVariableBinding<Int>
lateinit var testProvider: TestProvider

@Adaptive
@Suppress("unused")
fun propertyAccessTest() {
    val p = TestProvider().also { testProvider = it }
    propertyAccessor { p.i }
}

@Adaptive
fun <T> propertyAccessor(
    binding: AdaptiveStateVariableBinding<T>? = null,
    @Suppress("UNUSED_PARAMETER") selector: () -> T
) {
    checkNotNull(binding)
    checkNotNull(binding.propertyProvider)

    T1(binding.value as Int)

    @Suppress("UNCHECKED_CAST")
    testBinding = binding as AdaptiveStateVariableBinding<Int>
}

class PropertyAccessTest {

    @Test
    fun test() {
        val adapter = AdaptiveTestAdapter()

        AdaptivePropertyAccessBindingTest(adapter, null, 0).apply {
            create()
            mount()
        }

        testBinding.setValue(23, true)
        testProvider.i = 45

        assertEquals(
            adapter.expected(
                listOf(
                    //@formatter:off
                    TraceEvent("AdaptivePropertyAccessBindingTest", 2, "before-Create", ""),
                    TraceEvent("AdaptivePropertyAccessBindingTest", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptivePropertyAccessBindingTest", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptivePropertyAccessBindingTest", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptivePropertyAccessBindingTest", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [TestProvider()]"),
                    TraceEvent("AdaptivePropertyAccessor", 3, "before-Create", ""),
                    TraceEvent("AdaptivePropertyAccessor", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptivePropertyAccessBindingTest", 2, "before-Add-Binding", "binding: AdaptiveStateVariableBinding(2, 0, 0, 3, 0, [i], AdaptivePropertyMetadata(kotlin.Int))"),
                    TraceEvent("AdaptivePropertyAccessBindingTest", 2, "after-Add-Binding", "binding: AdaptiveStateVariableBinding(2, 0, 0, 3, 0, [i], AdaptivePropertyMetadata(kotlin.Int))"),
                    TraceEvent("AdaptivePropertyAccessor", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [AdaptiveStateVariableBinding(2, 0, 0, 3, 0, [i], AdaptivePropertyMetadata(kotlin.Int))]"),
                    TraceEvent("AdaptivePropertyAccessor", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [AdaptiveStateVariableBinding(2, 0, 0, 3, 0, [i], AdaptivePropertyMetadata(kotlin.Int))]"),
                    TraceEvent("AdaptivePropertyAccessor", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [AdaptiveStateVariableBinding(2, 0, 0, 3, 0, [i], AdaptivePropertyMetadata(kotlin.Int))]"),
                    TraceEvent("AdaptiveT1", 4, "before-Create", ""),
                    TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
                    TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
                    TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
                    TraceEvent("AdaptiveT1", 4, "after-Create", ""),
                    TraceEvent("AdaptivePropertyAccessor", 3, "after-Create", ""),
                    TraceEvent("AdaptivePropertyAccessBindingTest", 2, "after-Create", ""),
                    TraceEvent("AdaptivePropertyAccessBindingTest", 2, "before-Mount"),
                    TraceEvent("AdaptivePropertyAccessor", 3, "before-Mount"),
                    TraceEvent("AdaptiveT1", 4, "before-Mount"),
                    TraceEvent("AdaptiveT1", 4, "after-Mount"),
                    TraceEvent("AdaptivePropertyAccessor", 3, "after-Mount"),
                    TraceEvent("AdaptivePropertyAccessBindingTest", 2, "after-Mount"),
                    TraceEvent("AdaptivePropertyAccessor", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000001 state: [AdaptiveStateVariableBinding(2, 0, 0, 3, 0, [i], AdaptivePropertyMetadata(kotlin.Int))]"),
                    TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [12]"),
                    TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [23]"),
                    TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [23]"),
                    TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [23]"),
                    TraceEvent("AdaptivePropertyAccessor", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [AdaptiveStateVariableBinding(2, 0, 0, 3, 0, [i], AdaptivePropertyMetadata(kotlin.Int))]"),
                    TraceEvent("AdaptivePropertyAccessor", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000001 state: [AdaptiveStateVariableBinding(2, 0, 0, 3, 0, [i], AdaptivePropertyMetadata(kotlin.Int))]"),
                    TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [23]"),
                    TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [45]"),
                    TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [45]"),
                    TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [45]"),
                    TraceEvent("AdaptivePropertyAccessor", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [AdaptiveStateVariableBinding(2, 0, 0, 3, 0, [i], AdaptivePropertyMetadata(kotlin.Int))]")
                    //@formatter:on
                )
            ),
            adapter.actual(dumpCode = false)
        )
    }
}

class AdaptivePropertyAccessBindingTest(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int
) : AdaptiveTestFragment(adapter, parent, index, 1) {

    val dependencyMask_0_0 = 0x01 // fragment index: 0, state variable index: 0

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment {

        val fragment = when (declarationIndex) {
            0 -> AdaptivePropertyAccessor(parent.adapter, parent, declarationIndex)
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
                    setBinding(
                        indexInThis = 0,
                        descendant = fragment,
                        indexInTarget = 0,
                        path = arrayOf("i"),
                        boundType = "kotlin.Int"
                    )
                }
            }
        }
    }

    override fun genPatchInternal(): Boolean {
        state[0] = TestProvider().also { testProvider = it }
        return true
    }
}

class AdaptivePropertyAccessor(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int
) : AdaptiveTestFragment(adapter, parent, index, 1) {

    val dependencyMask_0_0 = 0x01 // fragment index: 0, state variable index: 0

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment {

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
                if (fragment.haveToPatch(closureMask, dependencyMask_0_0)) {
                    @Suppress("UNCHECKED_CAST")
                    fragment.setStateVariable(0, (getThisClosureVariable(0) as AdaptiveStateVariableBinding<Int>).value)
                }
            }
        }
    }

    override fun genPatchInternal(): Boolean {
        @Suppress("UNCHECKED_CAST")
        testBinding = state[0] as AdaptiveStateVariableBinding<Int>
        return true
    }

}