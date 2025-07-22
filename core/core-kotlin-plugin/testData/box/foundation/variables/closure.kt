/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adaptive
import `fun`.adaptive.foundation.testing.*
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.testing.AdaptiveTestAdapter

fun box() : String {

    val adapter = AdaptiveTestAdapter(printTrace = true)

    val a = 12

    adaptive(adapter) {
        T1(a)
    }

    return adapter.assert(listOf(
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null]"),
        TraceEvent("AdaptiveT1", 3, "before-Create", ""),
        TraceEvent("AdaptiveT1", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("AdaptiveT1", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12]"),
        TraceEvent("AdaptiveT1", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, 12]"),
        TraceEvent("AdaptiveT1", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null, 12]"),
        TraceEvent("AdaptiveT1", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 3, "before-Mount", ""),
        TraceEvent("AdaptiveT1", 3, "after-Mount", ""),
        TraceEvent("<root>", 2, "after-Mount", "")
    ))

//    val adapter: AdaptiveTestAdapter = AdaptiveTestAdapter(printTrace = true)
//    val a: Int = 12
//    adaptive(adapter = adapter, block = {
//
//        class AdaptiveRootClosure584(
//            adapter: AdaptiveAdapter, parent: AdaptiveFragment?, declarationIndex: Int
//        ) : AdaptiveFragment(adapter = adapter, parent = parent, declarationIndex = declarationIndex, stateSize = 1) {
//
//            override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? {
//                val tmp0: AdaptiveFragment = when {
//                    declarationIndex == 0 -> parent.adapter.actualize(name = "test:t1", parent = parent, index = declarationIndex, stateSize = 2)
//                    else -> invalidIndex(index = declarationIndex)
//                }
//                if (flags.and(other = 4) == 0) tmp0.create()
//                return tmp0
//            }
//
//            override fun genPatchDescendant(fragment: AdaptiveFragment) {
//                val tmp0: Int = fragment.getCreateClosureDirtyMask()
//                val tmp1: Int = fragment.declarationIndex
//                when {
//                    tmp1 == 0 -> { // BLOCK
//                        when {
//                            fragment.haveToPatch(closureDirtyMask = tmp0, dependencyMask = 0) -> fragment.setStateVariable(index = 1, value = a)
//                        }
//                    }
//
//                    else -> invalidIndex(index = tmp1)
//                }
//            }
//
//            override fun genPatchInternal(): Boolean {
//                val tmp0: Int = dirtyMask
//                dirtyMask = tmp0
//                return true
//            }
//
//        }
//
//        val tmp0: AdaptiveAdapter = it /*as AdaptiveAdapter */
//        val tmp1: AdaptiveRootClosure584 = AdaptiveRootClosure584(adapter = tmp0, parent = null, declarationIndex = 0)
//        tmp0.rootFragment = tmp1
//        tmp1.create()
//        tmp1.mount()
//    }
//    )
//
//    return "Fail"
}