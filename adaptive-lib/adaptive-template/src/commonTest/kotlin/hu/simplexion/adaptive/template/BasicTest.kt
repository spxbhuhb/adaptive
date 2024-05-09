/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.template

import hu.simplexion.adaptive.base.adaptive
import hu.simplexion.adaptive.base.registry.AdaptiveFragmentImplRegistryEntry
import hu.simplexion.adaptive.base.testing.AdaptiveT0
import hu.simplexion.adaptive.base.testing.AdaptiveT1
import hu.simplexion.adaptive.base.testing.AdaptiveTestAdapter
import hu.simplexion.adaptive.base.testing.TraceEvent
import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.wireformat.WireFormatRegistry
import kotlin.test.Test
import kotlin.test.assertEquals

class BasicTest {

    @Test
    fun test() {
        val adapter = AdaptiveTestAdapter(true)

        adapter.fragmentImplRegistry += AdaptiveFragmentImplRegistryEntry("T0") { a, p, i -> AdaptiveT0(a, p, i) }
        adapter.fragmentImplRegistry += AdaptiveFragmentImplRegistryEntry("T1") { a, p, i -> AdaptiveT1(a, p, i) }

        adaptive(adapter) {

            val template = AdaptiveTemplateData(
                UUID(),
                "test template",
                mapOf(
                    1 to AdaptiveTemplateVariable("i", 1, "I", "{\"i\" : 12}")
                ),
                mapOf(
                    12 to AdaptiveTemplateFragment("T0", 12, emptyList()),
                    23 to AdaptiveTemplateFragment(
                        "T1", 23, listOf(
                            AdaptiveTemplateMapping(1, 0)
                        )
                    )
                )
            )

            templateRealization(template)
        }

        assertEquals(
            adapter.expected(
                listOf(
                    //@formatter:off
                    TraceEvent("<root>", 2, "before-Create", ""),
                    TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
                    TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
                    TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
                    TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [AdaptiveTemplateData]"),
                    TraceEvent("AdaptiveTemplateRealization", 3, "before-Create", ""),
                    TraceEvent("AdaptiveTemplateRealization", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null]"),
                    TraceEvent("AdaptiveTemplateRealization", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [AdaptiveTemplateData, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null]"),
                    TraceEvent("AdaptiveTemplateRealization", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [AdaptiveTemplateData, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null]"),
                    TraceEvent("AdaptiveTemplateRealization", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [AdaptiveTemplateData, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null]"),
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
                    TraceEvent("AdaptiveTemplateRealization", 3, "after-Create", ""),
                    TraceEvent("<root>", 2, "after-Create", ""),
                    TraceEvent("<root>", 2, "before-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveTemplateRealization", 3, "before-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveT0", 4, "before-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveT0", 4, "after-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveT1", 5, "before-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveT1", 5, "after-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveTemplateRealization", 3, "after-Mount", "bridge: 1"),
                    TraceEvent("<root>", 2, "after-Mount", "bridge: 1")
                    //@formatter:on
                )
            ),
            adapter.actual(dumpCode = false)
        )
    }
}