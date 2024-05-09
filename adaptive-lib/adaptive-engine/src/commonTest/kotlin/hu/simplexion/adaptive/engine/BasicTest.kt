/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.engine

import hu.simplexion.adaptive.base.adaptive
import hu.simplexion.adaptive.base.testing.*
import hu.simplexion.adaptive.engine.model.AdaptiveFragmentMetaData
import hu.simplexion.adaptive.engine.model.AdaptivePatchInstruction
import hu.simplexion.adaptive.engine.patch.ConstIntPatchInstruction
import hu.simplexion.adaptive.engine.patch.ConstIntPatchInstructionData
import kotlin.test.Test
import kotlin.test.assertEquals

class BasicTest {

    @Test
    fun test() {
        val adapter = AdaptiveTestAdapter(true)

        adaptive(adapter) {

            val buildFunRegistry: AdaptiveMetaBuildFunRegistry<Any?> = mutableMapOf(
                "T0" to { a, p, i -> AdaptiveT0(a, p, i) },
                "T1" to { a, p, i -> AdaptiveT1(a, p, i) }
            )

            val patchFunRegistry: AdaptiveMetaPatchFunRegistry = mutableMapOf(
                "ConstIntPatchInstruction" to { ConstIntPatchInstruction(it as ConstIntPatchInstructionData) }
            )

            val metadata: AdaptiveMetaFragmentData = mutableMapOf(
                1 to AdaptiveFragmentMetaData("T0", emptyList()),
                4 to AdaptiveFragmentMetaData("T1",listOf(
                    AdaptivePatchInstruction("ConstIntPatchInstruction", ConstIntPatchInstructionData(0,12))
                )
                )
            )

            metaFragment(buildFunRegistry, patchFunRegistry, metadata)
        }

        assertEquals(
            adapter.expected(
                listOf(
                    //@formatter:off
                    TraceEvent("<root>", 2, "before-Create", ""),
                    TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null, null]"),
                    TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null, null]"),
                    TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null, null]"),
                    TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [{T0=Function3<hu.simplexion.adaptive.base.AdaptiveAdapter<java.lang.Object>, hu.simplexion.adaptive.base.AdaptiveFragment<java.lang.Object>, java.lang.Integer, hu.simplexion.adaptive.base.AdaptiveFragment<java.lang.Object>>, T1=Function3<hu.simplexion.adaptive.base.AdaptiveAdapter<java.lang.Object>, hu.simplexion.adaptive.base.AdaptiveFragment<java.lang.Object>, java.lang.Integer, hu.simplexion.adaptive.base.AdaptiveFragment<java.lang.Object>>}, {ConstIntPatchInstruction=Function1<java.lang.Object, hu.simplexion.adaptive.meta.patch.ConstIntPatchInstruction>}, {1=hu.simplexion.adaptive.meta.model.AdaptiveFragmentMetaData@c7045b9, 4=hu.simplexion.adaptive.meta.model.AdaptiveFragmentMetaData@f99f5e0}]"),
                    TraceEvent("AdaptiveEngine", 3, "before-Create", ""),
                    TraceEvent("AdaptiveEngine", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null, null]"),
                    TraceEvent("AdaptiveEngine", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [{T0=Function3<hu.simplexion.adaptive.base.AdaptiveAdapter<java.lang.Object>, hu.simplexion.adaptive.base.AdaptiveFragment<java.lang.Object>, java.lang.Integer, hu.simplexion.adaptive.base.AdaptiveFragment<java.lang.Object>>, T1=Function3<hu.simplexion.adaptive.base.AdaptiveAdapter<java.lang.Object>, hu.simplexion.adaptive.base.AdaptiveFragment<java.lang.Object>, java.lang.Integer, hu.simplexion.adaptive.base.AdaptiveFragment<java.lang.Object>>}, {ConstIntPatchInstruction=Function1<java.lang.Object, hu.simplexion.adaptive.meta.patch.ConstIntPatchInstruction>}, {1=hu.simplexion.adaptive.meta.model.AdaptiveFragmentMetaData@c7045b9, 4=hu.simplexion.adaptive.meta.model.AdaptiveFragmentMetaData@f99f5e0}]"),
                    TraceEvent("AdaptiveEngine", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [{T0=Function3<hu.simplexion.adaptive.base.AdaptiveAdapter<java.lang.Object>, hu.simplexion.adaptive.base.AdaptiveFragment<java.lang.Object>, java.lang.Integer, hu.simplexion.adaptive.base.AdaptiveFragment<java.lang.Object>>, T1=Function3<hu.simplexion.adaptive.base.AdaptiveAdapter<java.lang.Object>, hu.simplexion.adaptive.base.AdaptiveFragment<java.lang.Object>, java.lang.Integer, hu.simplexion.adaptive.base.AdaptiveFragment<java.lang.Object>>}, {ConstIntPatchInstruction=Function1<java.lang.Object, hu.simplexion.adaptive.meta.patch.ConstIntPatchInstruction>}, {1=hu.simplexion.adaptive.meta.model.AdaptiveFragmentMetaData@c7045b9, 4=hu.simplexion.adaptive.meta.model.AdaptiveFragmentMetaData@f99f5e0}]"),
                    TraceEvent("AdaptiveEngine", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [{T0=Function3<hu.simplexion.adaptive.base.AdaptiveAdapter<java.lang.Object>, hu.simplexion.adaptive.base.AdaptiveFragment<java.lang.Object>, java.lang.Integer, hu.simplexion.adaptive.base.AdaptiveFragment<java.lang.Object>>, T1=Function3<hu.simplexion.adaptive.base.AdaptiveAdapter<java.lang.Object>, hu.simplexion.adaptive.base.AdaptiveFragment<java.lang.Object>, java.lang.Integer, hu.simplexion.adaptive.base.AdaptiveFragment<java.lang.Object>>}, {ConstIntPatchInstruction=Function1<java.lang.Object, hu.simplexion.adaptive.meta.patch.ConstIntPatchInstruction>}, {1=hu.simplexion.adaptive.meta.model.AdaptiveFragmentMetaData@c7045b9, 4=hu.simplexion.adaptive.meta.model.AdaptiveFragmentMetaData@f99f5e0}]"),
                    TraceEvent("AdaptiveT0", 4, "before-Create", ""),
                    TraceEvent("AdaptiveT0", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
                    TraceEvent("AdaptiveT0", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
                    TraceEvent("AdaptiveT0", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
                    TraceEvent("AdaptiveT0", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
                    TraceEvent("AdaptiveT0", 4, "after-Create", ""),
                    TraceEvent("AdaptiveT1", 5, "before-Create", ""),
                    TraceEvent("AdaptiveT1", 5, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveT1", 5, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
                    TraceEvent("AdaptiveT1", 5, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
                    TraceEvent("AdaptiveT1", 5, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
                    TraceEvent("AdaptiveT1", 5, "after-Create", ""),
                    TraceEvent("AdaptiveEngine", 3, "after-Create", ""),
                    TraceEvent("<root>", 2, "after-Create", ""),
                    TraceEvent("<root>", 2, "before-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveEngine", 3, "before-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveEngine", 3, "after-Mount", "bridge: 1"),
                    TraceEvent("<root>", 2, "after-Mount", "bridge: 1")
                    //@formatter:on
                )
            ),
            adapter.actual(dumpCode = true)
        )
    }
}