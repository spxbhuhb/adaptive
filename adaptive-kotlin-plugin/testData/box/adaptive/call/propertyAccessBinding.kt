/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.adaptive.base.*
import hu.simplexion.adaptive.base.binding.*
import hu.simplexion.adaptive.base.testing.*

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
            else -> Unit
        }
    }

    override fun setValue(path: Array<String>, value: Any?, fromBinding: AdaptiveStateVariableBinding<*>) {
        when {
            path[0] == "i" -> values[0] = value
        }
        bindings.forEach { if (it != fromBinding && it.path.contentEquals(path)) it.setValue(path, false) }
    }

    override fun toString(): String {
        return "TestProvider()"
    }
}

lateinit var testBinding : AdaptiveStateVariableBinding<Int>
lateinit var testProvider: TestProvider

fun Adaptive.propertyAccessTest() {
    val p = TestProvider()
    testProvider = p
    propertyAccessor { p.i }
}

fun <T> Adaptive.propertyAccessor(
    binding: AdaptiveStateVariableBinding<T>? = null,
    @Suppress("UNUSED_PARAMETER") selector: () -> T
) {
    checkNotNull(binding)

    @Suppress("UNCHECKED_CAST")
    testBinding = binding as AdaptiveStateVariableBinding<Int>

    T1(binding.value as Int)
}

fun box() : String {

    AdaptiveAdapterRegistry.register(AdaptiveTestAdapterFactory)

    adaptive {
        propertyAccessTest()
    }

    testBinding.setValue(23, true)
    testProvider.i = 45

    return AdaptiveTestAdapter.assert(listOf(
        //@formatter:off
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptivePropertyAccessTest", 3, "before-Create", ""),
        TraceEvent("AdaptivePropertyAccessTest", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptivePropertyAccessTest", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptivePropertyAccessTest", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptivePropertyAccessTest", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [TestProvider()]"),
        TraceEvent("AdaptivePropertyAccessor", 4, "before-Create", ""),
        TraceEvent("AdaptivePropertyAccessor", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptivePropertyAccessTest", 3, "before-Add-Binding", "binding: AdaptiveStateVariableBinding(3, 0, 0, 4, 0, [i], -1, AdaptivePropertyMetadata(kotlin.Int))"),
        TraceEvent("AdaptivePropertyAccessTest", 3, "after-Add-Binding", "binding: AdaptiveStateVariableBinding(3, 0, 0, 4, 0, [i], -1, AdaptivePropertyMetadata(kotlin.Int))"),
        TraceEvent("AdaptivePropertyAccessor", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [AdaptiveStateVariableBinding(3, 0, 0, 4, 0, [i], -1, AdaptivePropertyMetadata(kotlin.Int))]"),
        TraceEvent("AdaptivePropertyAccessor", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [AdaptiveStateVariableBinding(3, 0, 0, 4, 0, [i], -1, AdaptivePropertyMetadata(kotlin.Int))]"),
        TraceEvent("AdaptivePropertyAccessor", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [AdaptiveStateVariableBinding(3, 0, 0, 4, 0, [i], -1, AdaptivePropertyMetadata(kotlin.Int))]"),
        TraceEvent("AdaptiveT1", 5, "before-Create", ""),
        TraceEvent("AdaptiveT1", 5, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
        TraceEvent("AdaptiveT1", 5, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
        TraceEvent("AdaptiveT1", 5, "after-Create", ""),
        TraceEvent("AdaptivePropertyAccessor", 4, "after-Create", ""),
        TraceEvent("AdaptivePropertyAccessTest", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptivePropertyAccessTest", 3, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptivePropertyAccessor", 4, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveT1", 5, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveT1", 5, "after-Mount", "bridge: 1"),
        TraceEvent("AdaptivePropertyAccessor", 4, "after-Mount", "bridge: 1"),
        TraceEvent("AdaptivePropertyAccessTest", 3, "after-Mount", "bridge: 1"),
        TraceEvent("<root>", 2, "after-Mount", "bridge: 1"),
        TraceEvent("AdaptivePropertyAccessor", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000001 state: [AdaptiveStateVariableBinding(3, 0, 0, 4, 0, [i], -1, AdaptivePropertyMetadata(kotlin.Int))]"),
        TraceEvent("AdaptiveT1", 5, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [12]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [23]"),
        TraceEvent("AdaptiveT1", 5, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [23]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [23]"),
        TraceEvent("AdaptivePropertyAccessor", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [AdaptiveStateVariableBinding(3, 0, 0, 4, 0, [i], -1, AdaptivePropertyMetadata(kotlin.Int))]"),
        TraceEvent("AdaptivePropertyAccessor", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000001 state: [AdaptiveStateVariableBinding(3, 0, 0, 4, 0, [i], -1, AdaptivePropertyMetadata(kotlin.Int))]"),
        TraceEvent("AdaptiveT1", 5, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [23]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [45]"),
        TraceEvent("AdaptiveT1", 5, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [45]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [45]"),
        TraceEvent("AdaptivePropertyAccessor", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [AdaptiveStateVariableBinding(3, 0, 0, 4, 0, [i], -1, AdaptivePropertyMetadata(kotlin.Int))]")
        //@formatter:on
    ))
}