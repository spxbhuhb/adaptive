/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.base

import hu.simplexion.adaptive.base.binding.AdaptivePropertyProvider
import hu.simplexion.adaptive.base.binding.AdaptiveStateVariableBinding
import hu.simplexion.adaptive.base.testing.*
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

    override fun setValue(path: Array<String>, value: Any?, fromBinding: AdaptiveStateVariableBinding<*>) {
        when {
            path[0] == "i" -> values[0] = value
            else -> throw AssertionError("unknown property, path=$path")
        }
        bindings.forEach { if (it != fromBinding && it.path.contentEquals(path)) it.setValue(path, false) }
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
        val root = AdaptiveTestBridge(1)

        AdaptivePropertyAccessBindingTest(adapter, null, 0).apply {
            create()
            mount(root)
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
                    TraceEvent("AdaptivePropertyAccessBindingTest", 2, "before-Add-Binding", "binding: AdaptiveStateVariableBinding(2, 0, 0, 3, 0, [i], -1, AdaptivePropertyMetadata(kotlin.Int))"),
                    TraceEvent("AdaptivePropertyAccessBindingTest", 2, "after-Add-Binding", "binding: AdaptiveStateVariableBinding(2, 0, 0, 3, 0, [i], -1, AdaptivePropertyMetadata(kotlin.Int))"),
                    TraceEvent("AdaptivePropertyAccessor", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [AdaptiveStateVariableBinding(2, 0, 0, 3, 0, [i], -1, AdaptivePropertyMetadata(kotlin.Int))]"),
                    TraceEvent("AdaptivePropertyAccessor", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [AdaptiveStateVariableBinding(2, 0, 0, 3, 0, [i], -1, AdaptivePropertyMetadata(kotlin.Int))]"),
                    TraceEvent("AdaptivePropertyAccessor", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [AdaptiveStateVariableBinding(2, 0, 0, 3, 0, [i], -1, AdaptivePropertyMetadata(kotlin.Int))]"),
                    TraceEvent("AdaptiveT1", 4, "before-Create", ""),
                    TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
                    TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
                    TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
                    TraceEvent("AdaptiveT1", 4, "after-Create", ""),
                    TraceEvent("AdaptivePropertyAccessor", 3, "after-Create", ""),
                    TraceEvent("AdaptivePropertyAccessBindingTest", 2, "after-Create", ""),
                    TraceEvent("AdaptivePropertyAccessBindingTest", 2, "before-Mount", "bridge: 1"),
                    TraceEvent("AdaptivePropertyAccessor", 3, "before-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveT1", 4, "before-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveT1", 4, "after-Mount", "bridge: 1"),
                    TraceEvent("AdaptivePropertyAccessor", 3, "after-Mount", "bridge: 1"),
                    TraceEvent("AdaptivePropertyAccessBindingTest", 2, "after-Mount", "bridge: 1"),
                    TraceEvent("AdaptivePropertyAccessor", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000001 state: [AdaptiveStateVariableBinding(2, 0, 0, 3, 0, [i], -1, AdaptivePropertyMetadata(kotlin.Int))]"),
                    TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [12]"),
                    TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [23]"),
                    TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [23]"),
                    TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [23]"),
                    TraceEvent("AdaptivePropertyAccessor", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [AdaptiveStateVariableBinding(2, 0, 0, 3, 0, [i], -1, AdaptivePropertyMetadata(kotlin.Int))]"),
                    TraceEvent("AdaptivePropertyAccessor", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000001 state: [AdaptiveStateVariableBinding(2, 0, 0, 3, 0, [i], -1, AdaptivePropertyMetadata(kotlin.Int))]"),
                    TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [23]"),
                    TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [45]"),
                    TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [45]"),
                    TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [45]"),
                    TraceEvent("AdaptivePropertyAccessor", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [AdaptiveStateVariableBinding(2, 0, 0, 3, 0, [i], -1, AdaptivePropertyMetadata(kotlin.Int))]")
                    //@formatter:on
                )
            ),
            adapter.actual(dumpCode = false)
        )
    }
}

class AdaptivePropertyAccessBindingTest(
    adapter: AdaptiveAdapter<TestNode>,
    parent: AdaptiveFragment<TestNode>?,
    index: Int
) : AdaptiveFragment<TestNode>(adapter, parent, index, 1) {

    val dependencyMask_0_0 = 0x01 // fragment index: 0, state variable index: 0

    override fun genBuild(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode> {

        val fragment = when (declarationIndex) {
            0 -> AdaptivePropertyAccessor(adapter, parent, declarationIndex)
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

    override fun genPatchInternal() {
        state[0] = TestProvider().also { testProvider = it }
    }
}

class AdaptivePropertyAccessor(
    adapter: AdaptiveAdapter<TestNode>,
    parent: AdaptiveFragment<TestNode>?,
    index: Int
) : AdaptiveFragment<TestNode>(adapter, parent, index, 1) {

    val dependencyMask_0_0 = 0x01 // fragment index: 0, state variable index: 0

    override fun genBuild(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode> {

        val fragment = when (declarationIndex) {
            0 -> AdaptiveT1(adapter, parent, declarationIndex)
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
                    @Suppress("UNCHECKED_CAST")
                    fragment.setStateVariable(0, (getThisClosureVariable(0) as AdaptiveStateVariableBinding<Int>).value)
                }
            }
        }
    }

    override fun genPatchInternal() {
        @Suppress("UNCHECKED_CAST")
        testBinding = state[0] as AdaptiveStateVariableBinding<Int>
    }

}