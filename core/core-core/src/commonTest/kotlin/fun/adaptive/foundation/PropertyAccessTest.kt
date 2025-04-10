/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.foundation

// IMPORTANT This test is commented out because we have it covered by the compiler plugin.
// It is broken because the instruction refactor moved instructions into the very beginning of the state.
// That changed all dependency masks and indices which is a pain to update manually.
// I've left it here so it can be seen, but for now i don't really want to spend time on it pointlessly.


//import `fun`.adaptive.foundation.binding.AdaptivePropertyProvider
//import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
//import `fun`.adaptive.foundation.binding.PropertySelector
//import `fun`.adaptive.foundation.testing.*
//import kotlin.test.Test
//import kotlin.test.assertEquals
//
//class TestProvider : AdaptivePropertyProvider {
//
//    val values = arrayOf<Any?>(12)
//
//    var i : Int
//        get() = values[0] as Int
//        set(v) {
//            values[0] = v
//            bindings.forEach { it.setValue(v, false) }
//        }
//
//    val bindings = mutableListOf<AdaptiveStateVariableBinding<*>>()
//
//    override fun addBinding(binding: AdaptiveStateVariableBinding<*>) {
//        bindings += binding
//    }
//
//    override fun removeBinding(binding: AdaptiveStateVariableBinding<*>) {
//        bindings += binding
//    }
//
//    override fun getValue(path: Array<String>): Any? {
//        return when {
//            path[0] == "i" -> values[0]
//            else -> throw AssertionError("unknown property, path=$path")
//        }
//    }
//
//    override fun setValue(path: Array<String>, value: Any?) {
//        when {
//            path[0] == "i" -> values[0] = value
//            else -> throw AssertionError("unknown property, path=$path")
//        }
//    }
//
//    override fun toString(): String {
//        return "TestProvider()"
//    }
//}
//
//lateinit var testBinding : AdaptiveStateVariableBinding<Int>
//lateinit var testProvider: TestProvider
//
//@Adaptive
//@Suppress("unused")
//fun propertyAccessTest() {
//    val p = TestProvider().also { testProvider = it }
//    propertyAccessor { p.i }
//}
//
//@Adaptive
//fun <T> propertyAccessor(
//    binding: AdaptiveStateVariableBinding<T>? = null,
//    @Suppress("unused")
//    @PropertySelector
//    selector: () -> T
//) {
//    checkNotNull(binding)
//    checkNotNull(binding.propertyProvider)
//
//    T1(binding.value as Int)
//
//    @Suppress("UNCHECKED_CAST")
//    testBinding = binding as AdaptiveStateVariableBinding<Int>
//}
//
//class PropertyAccessTest {
//
//    @Test
//    fun test() {
//        val adapter = AdaptiveTestAdapter()
//
//        AdaptivePropertyAccessBindingTest(adapter, null, 0).apply {
//            create()
//            mount()
//        }
//
//        testBinding.setValue(23, true)
//        testProvider.i = 45
//
//        assertEquals(
//            adapter.expected(
//                listOf(
//                    //@formatter:off
//                    TraceEvent("AdaptivePropertyAccessBindingTest", 2, "before-Create", ""),
//                    TraceEvent("AdaptivePropertyAccessBindingTest", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
//                    TraceEvent("AdaptivePropertyAccessBindingTest", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
//                    TraceEvent("AdaptivePropertyAccessBindingTest", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
//                    TraceEvent("AdaptivePropertyAccessBindingTest", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [TestProvider()]"),
//                    TraceEvent("AdaptivePropertyAccessor", 3, "before-Create", ""),
//                    TraceEvent("AdaptivePropertyAccessor", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
//                    TraceEvent("AdaptivePropertyAccessBindingTest", 2, "before-Add-Binding", "binding: AdaptiveStateVariableBinding(2, 0, 0, 3, 0, [i], kotlin.Int, null)"),
//                    TraceEvent("AdaptivePropertyAccessBindingTest", 2, "after-Add-Binding", "binding: AdaptiveStateVariableBinding(2, 0, 0, 3, 0, [i], kotlin.Int, null)"),
//                    TraceEvent("AdaptivePropertyAccessor", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [AdaptiveStateVariableBinding(2, 0, 0, 3, 0, [i], kotlin.Int, null)]"),
//                    TraceEvent("AdaptivePropertyAccessor", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [AdaptiveStateVariableBinding(2, 0, 0, 3, 0, [i], kotlin.Int, null)]"),
//                    TraceEvent("AdaptivePropertyAccessor", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [AdaptiveStateVariableBinding(2, 0, 0, 3, 0, [i], kotlin.Int, null)]"),
//                    TraceEvent("AdaptiveT1", 4, "before-Create", ""),
//                    TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
//                    TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12, null]"),
//                    TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12, null]"),
//                    TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12, null]"),
//                    TraceEvent("AdaptiveT1", 4, "after-Create", ""),
//                    TraceEvent("AdaptivePropertyAccessor", 3, "after-Create", ""),
//                    TraceEvent("AdaptivePropertyAccessBindingTest", 2, "after-Create", ""),
//                    TraceEvent("AdaptivePropertyAccessBindingTest", 2, "before-Mount", ""),
//                    TraceEvent("AdaptivePropertyAccessor", 3, "before-Mount", ""),
//                    TraceEvent("AdaptiveT1", 4, "before-Mount", ""),
//                    TraceEvent("AdaptiveT1", 4, "after-Mount", ""),
//                    TraceEvent("AdaptivePropertyAccessor", 3, "after-Mount", ""),
//                    TraceEvent("AdaptivePropertyAccessBindingTest", 2, "after-Mount", ""),
//                    TraceEvent("AdaptivePropertyAccessor", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000001 state: [AdaptiveStateVariableBinding(2, 0, 0, 3, 0, [i], kotlin.Int, null)]"),
//                    TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [12, null]"),
//                    TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [23, null]"),
//                    TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [23, null]"),
//                    TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [23, null]"),
//                    TraceEvent("AdaptivePropertyAccessor", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [AdaptiveStateVariableBinding(2, 0, 0, 3, 0, [i], kotlin.Int, null)]"),
//                    TraceEvent("AdaptivePropertyAccessor", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000001 state: [AdaptiveStateVariableBinding(2, 0, 0, 3, 0, [i], kotlin.Int, null)]"),
//                    TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [23, null]"),
//                    TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [45, null]"),
//                    TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [45, null]"),
//                    TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [45, null]"),
//                    TraceEvent("AdaptivePropertyAccessor", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [AdaptiveStateVariableBinding(2, 0, 0, 3, 0, [i], kotlin.Int, null)]")
//                    //@formatter:on
//                )
//            ),
//            adapter.actual(dumpCode = true)
//        )
//    }
//}
//
//class AdaptivePropertyAccessBindingTest(
//    adapter: AdaptiveAdapter,
//    parent: AdaptiveFragment?,
//    index: Int
//) : AdaptiveTestFragment(adapter, parent, index, 1) {
//
//    val dependencyMask_0_1 = 0x02 // fragment index: 0, state variable index: 0
//
//    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment {
//
//        val fragment = when (declarationIndex) {
//            0 -> AdaptivePropertyAccessor(parent.adapter, parent, declarationIndex)
//            else -> invalidIndex(declarationIndex) // throws exception
//        }
//
//        fragment.create()
//
//        return fragment
//    }
//
//    override fun genPatchDescendant(fragment: AdaptiveFragment) {
//
//        val closureMask = fragment.getCreateClosureDirtyMask()
//
//        when (fragment.declarationIndex) {
//            0 -> {
//                if (fragment.haveToPatch(closureMask, dependencyMask_0_1)) {
//                    setBinding(
//                        indexInThis = 1,
//                        descendant = fragment,
//                        indexInTarget = 1,
//                        path = arrayOf("i"),
//                        boundType = "kotlin.Int",
//                        adatCompanion = null
//                    )
//                }
//            }
//        }
//    }
//
//    override fun genPatchInternal(): Boolean {
//        set(0, TestProvider().also { testProvider = it })
//        return true
//    }
//}
//
//class AdaptivePropertyAccessor(
//    adapter: AdaptiveAdapter,
//    parent: AdaptiveFragment?,
//    index: Int
//) : AdaptiveTestFragment(adapter, parent, index, 1) {
//
//    val dependencyMask_0_1 = 0x02 // fragment index: 0, state variable index: 1
//
//    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment {
//
//        val fragment = when (declarationIndex) {
//            0 -> AdaptiveT1(parent.adapter, parent, declarationIndex)
//            else -> invalidIndex(declarationIndex) // throws exception
//        }
//
//        fragment.create()
//
//        return fragment
//    }
//
//    override fun genPatchDescendant(fragment: AdaptiveFragment) {
//
//        val closureMask = fragment.getCreateClosureDirtyMask()
//
//        when (fragment.declarationIndex) {
//            0 -> {
//                if (fragment.haveToPatch(closureMask, dependencyMask_0_1)) {
//                    @Suppress("UNCHECKED_CAST")
//                    fragment.setStateVariable(1, (getThisClosureVariable(1) as AdaptiveStateVariableBinding<Int>).value)
//                }
//            }
//        }
//    }
//
//    override fun genPatchInternal(): Boolean {
//        @Suppress("UNCHECKED_CAST")
//        testBinding = get<AdaptiveStateVariableBinding<Int>>(1)
//        return true
//    }
//
//}