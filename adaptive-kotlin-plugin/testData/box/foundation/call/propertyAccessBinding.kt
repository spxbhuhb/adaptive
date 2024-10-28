/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.foundation.*
import `fun`.adaptive.foundation.binding.*
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.testing.*

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

    override fun setValue(path: Array<String>, value: Any?) {
        when {
            path[0] == "i" -> values[0] = value
        }
        bindings.forEach { it.setValue(path, false) }
    }

    override fun toString(): String {
        return "TestProvider()"
    }
}

lateinit var testBinding : AdaptiveStateVariableBinding<Int>
lateinit var testProvider: TestProvider

@Adaptive
fun propertyAccessTest() {
    val p = TestProvider()
    testProvider = p
    propertyAccessor { p.i }
}

@Adaptive
fun <T> propertyAccessor(
    binding: AdaptiveStateVariableBinding<T>? = null,
    @Suppress("UNUSED_PARAMETER")
    @PropertySelector
    selector: () -> T
) {
    checkNotNull(binding)

    @Suppress("UNCHECKED_CAST")
    testBinding = binding as AdaptiveStateVariableBinding<Int>

    T1(binding.value as Int)
}

fun box() : String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        propertyAccessTest()
    }

    testBinding.setValue(23, true)
    testProvider.i = 45

    return adapter.assert(listOf(
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
        TraceEvent("AdaptivePropertyAccessTest", 3, "before-Add-Binding", "binding: AdaptiveStateVariableBinding(3, 0, 0, 4, 0, [i], I, null)"),
        TraceEvent("AdaptivePropertyAccessTest", 3, "after-Add-Binding", "binding: AdaptiveStateVariableBinding(3, 0, 0, 4, 0, [i], I, null)"),
        TraceEvent("AdaptivePropertyAccessor", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [AdaptiveStateVariableBinding(3, 0, 0, 4, 0, [i], I, null)]"),
        TraceEvent("AdaptivePropertyAccessor", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [AdaptiveStateVariableBinding(3, 0, 0, 4, 0, [i], I, null)]"),
        TraceEvent("AdaptivePropertyAccessor", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [AdaptiveStateVariableBinding(3, 0, 0, 4, 0, [i], I, null)]"),
        TraceEvent("AdaptiveT1", 5, "before-Create", ""),
        TraceEvent("AdaptiveT1", 5, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
        TraceEvent("AdaptiveT1", 5, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
        TraceEvent("AdaptiveT1", 5, "after-Create", ""),
        TraceEvent("AdaptivePropertyAccessor", 4, "after-Create", ""),
        TraceEvent("AdaptivePropertyAccessTest", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", ""),
        TraceEvent("AdaptivePropertyAccessTest", 3, "before-Mount", ""),
        TraceEvent("AdaptivePropertyAccessor", 4, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 5, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 5, "after-Mount", ""),
        TraceEvent("AdaptivePropertyAccessor", 4, "after-Mount", ""),
        TraceEvent("AdaptivePropertyAccessTest", 3, "after-Mount", ""),
        TraceEvent("<root>", 2, "after-Mount", ""),
        TraceEvent("AdaptivePropertyAccessor", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000001 state: [AdaptiveStateVariableBinding(3, 0, 0, 4, 0, [i], I, null)]"),
        TraceEvent("AdaptiveT1", 5, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [12]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [23]"),
        TraceEvent("AdaptiveT1", 5, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [23]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [23]"),
        TraceEvent("AdaptivePropertyAccessor", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [AdaptiveStateVariableBinding(3, 0, 0, 4, 0, [i], I, null)]"),
        TraceEvent("AdaptivePropertyAccessor", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000001 state: [AdaptiveStateVariableBinding(3, 0, 0, 4, 0, [i], I, null)]"),
        TraceEvent("AdaptiveT1", 5, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [23]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [23]"),
        TraceEvent("AdaptiveT1", 5, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [23]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [23]"),
        TraceEvent("AdaptivePropertyAccessor", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [AdaptiveStateVariableBinding(3, 0, 0, 4, 0, [i], I, null)]"),
        TraceEvent("AdaptivePropertyAccessor", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000001 state: [AdaptiveStateVariableBinding(3, 0, 0, 4, 0, [i], I, null)]"),
        TraceEvent("AdaptiveT1", 5, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [23]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [45]"),
        TraceEvent("AdaptiveT1", 5, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [45]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [45]"),
        TraceEvent("AdaptivePropertyAccessor", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [AdaptiveStateVariableBinding(3, 0, 0, 4, 0, [i], I, null)]")
        //@formatter:on
    ))
}