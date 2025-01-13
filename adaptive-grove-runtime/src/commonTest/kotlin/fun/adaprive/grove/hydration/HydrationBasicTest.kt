package `fun`.adaprive.grove.hydration

import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.foundation.testing.TraceEvent
import `fun`.adaptive.foundation.testing.test
import `fun`.adaptive.grove.api.GroveFragmentFactory
import `fun`.adaptive.grove.api.hydrated
import `fun`.adaptive.grove.hydration.model.BuildItem
import `fun`.adaptive.grove.hydration.model.DehydratedFragment
import `fun`.adaptive.grove.hydration.model.PatchDescendantItem
import `fun`.adaptive.grove.hydration.model.PatchDescendantStep
import kotlin.test.Test
import kotlin.test.assertEquals

class HydrationBasicTest {

    @Test
    fun empty() {
        test {
            it.fragmentFactory += GroveFragmentFactory
            hydrated(DehydratedFragment(emptyList(), emptyList(), emptyList()))
        }.also {
            //@formatter:off
            assertEquals("OK", it.assert(listOf(
                TraceEvent("<root>", 2, "before-Create", ""),
                TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
                TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
                TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
                TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
                TraceEvent("HydratedFragment", 3, "before-Create", ""),
                TraceEvent("HydratedFragment", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                TraceEvent("HydratedFragment", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [DehydratedFragment(build=[], patchInternal=[], patchDescendant=[])]"),
                TraceEvent("HydratedFragment", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [DehydratedFragment(build=[], patchInternal=[], patchDescendant=[])]"),
                TraceEvent("HydratedFragment", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [DehydratedFragment(build=[], patchInternal=[], patchDescendant=[])]"),
                TraceEvent("HydratedFragment", 3, "after-Create", ""),
                TraceEvent("<root>", 2, "after-Create", ""),
                TraceEvent("<root>", 2, "before-Mount", ""),
                TraceEvent("HydratedFragment", 3, "before-Mount", ""),
                TraceEvent("HydratedFragment", 3, "after-Mount", ""),
                TraceEvent("<root>", 2, "after-Mount", "")
            )))
            //@formatter:on
        }
    }

    @Test
    fun t0() {
        val dehydrated = DehydratedFragment(
            listOf(
                BuildItem(0, "test:t0")
            ),
            emptyList(),
            emptyList()
        )

        test(printTrace = true) {
            it.fragmentFactory += GroveFragmentFactory
            hydrated(dehydrated)
        }.also {
            //@formatter:off
            assertEquals("OK", it.assert(listOf(
                TraceEvent("<root>", 2, "before-Create", ""),
                TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
                TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
                TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
                TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
                TraceEvent("HydratedFragment", 3, "before-Create", ""),
                TraceEvent("HydratedFragment", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                TraceEvent("HydratedFragment", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [DehydratedFragment(build=[BuildItem(index=0, key=test:t0)], patchInternal=[], patchDescendant=[])]"),
                TraceEvent("HydratedFragment", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [DehydratedFragment(build=[BuildItem(index=0, key=test:t0)], patchInternal=[], patchDescendant=[])]"),
                TraceEvent("HydratedFragment", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [DehydratedFragment(build=[BuildItem(index=0, key=test:t0)], patchInternal=[], patchDescendant=[])]"),
                TraceEvent("AdaptiveT0", 4, "before-Create", ""),
                TraceEvent("AdaptiveT0", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
                TraceEvent("AdaptiveT0", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
                TraceEvent("AdaptiveT0", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
                TraceEvent("AdaptiveT0", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
                TraceEvent("AdaptiveT0", 4, "after-Create", ""),
                TraceEvent("HydratedFragment", 3, "after-Create", ""),
                TraceEvent("<root>", 2, "after-Create", ""),
                TraceEvent("<root>", 2, "before-Mount", ""),
                TraceEvent("HydratedFragment", 3, "before-Mount", ""),
                TraceEvent("AdaptiveT0", 4, "before-Mount", ""),
                TraceEvent("AdaptiveT0", 4, "after-Mount", ""),
                TraceEvent("HydratedFragment", 3, "after-Mount", ""),
                TraceEvent("<root>", 2, "after-Mount", "")
            )))
            //@formatter:on
        }
    }

    @Test
    fun t1() {
        val dehydrated = DehydratedFragment(
            listOf(
                BuildItem(0, "test:t1")
            ),
            emptyList(),
            listOf(
                PatchDescendantItem(
                    0,
                    listOf(
                        PatchDescendantStep(
                            dependencyMask = 1,
                            stateVariableIndex = 0,
                            value = 123
                        )
                    )
                )
            )
        )

        test(printTrace = true) {
            it.fragmentFactory += GroveFragmentFactory
            hydrated(dehydrated)
        }.also {
            //@formatter:off
            assertEquals("OK", it.assert(listOf(
                TraceEvent("<root>", 2, "before-Create", ""),
                TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
                TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
                TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
                TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
                TraceEvent("HydratedFragment", 3, "before-Create", ""),
                TraceEvent("HydratedFragment", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                TraceEvent("HydratedFragment", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [DehydratedFragment(build=[BuildItem(index=0, key=test:t1)], patchInternal=[], patchDescendant=[PatchDescendantItem(declarationIndex=0, steps=[PatchDescendantStep(dependencyMask=1, stateVariableIndex=0, value=123)])])]"),
                TraceEvent("HydratedFragment", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [DehydratedFragment(build=[BuildItem(index=0, key=test:t1)], patchInternal=[], patchDescendant=[PatchDescendantItem(declarationIndex=0, steps=[PatchDescendantStep(dependencyMask=1, stateVariableIndex=0, value=123)])])]"),
                TraceEvent("HydratedFragment", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [DehydratedFragment(build=[BuildItem(index=0, key=test:t1)], patchInternal=[], patchDescendant=[PatchDescendantItem(declarationIndex=0, steps=[PatchDescendantStep(dependencyMask=1, stateVariableIndex=0, value=123)])])]"),
                TraceEvent("AdaptiveT1", 4, "before-Create", ""),
                TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [123]"),
                TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [123]"),
                TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [123]"),
                TraceEvent("AdaptiveT1", 4, "after-Create", ""),
                TraceEvent("HydratedFragment", 3, "after-Create", ""),
                TraceEvent("<root>", 2, "after-Create", ""),
                TraceEvent("<root>", 2, "before-Mount", ""),
                TraceEvent("HydratedFragment", 3, "before-Mount", ""),
                TraceEvent("AdaptiveT1", 4, "before-Mount", ""),
                TraceEvent("AdaptiveT1", 4, "after-Mount", ""),
                TraceEvent("HydratedFragment", 3, "after-Mount", ""),
                TraceEvent("<root>", 2, "after-Mount", "")
            )))
            //@formatter:on
        }
    }

    @Test
    fun instruction() {
        val dehydrated = DehydratedFragment(
            listOf(
                BuildItem(0, "test:t1")
            ),
            emptyList(),
            listOf(
                PatchDescendantItem(
                    0,
                    listOf(
                        PatchDescendantStep(
                            dependencyMask = 1,
                            stateVariableIndex = 0,
                            value = listOf(name("hello"))
                        )
                    )
                )
            )
        )

        test(printTrace = true) {
            it.fragmentFactory += GroveFragmentFactory
            hydrated(dehydrated)
        }.also {
            //@formatter:off
            assertEquals("OK", it.assert(listOf(
                TraceEvent("<root>", 2, "before-Create", ""),
                TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
                TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
                TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
                TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
                TraceEvent("HydratedFragment", 3, "before-Create", ""),
                TraceEvent("HydratedFragment", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                TraceEvent("HydratedFragment", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [DehydratedFragment(build=[BuildItem(index=0, key=test:t1)], patchInternal=[], patchDescendant=[PatchDescendantItem(declarationIndex=0, steps=[PatchDescendantStep(dependencyMask=1, stateVariableIndex=0, value=[Name(name=hello)])])])]"),
                TraceEvent("HydratedFragment", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [DehydratedFragment(build=[BuildItem(index=0, key=test:t1)], patchInternal=[], patchDescendant=[PatchDescendantItem(declarationIndex=0, steps=[PatchDescendantStep(dependencyMask=1, stateVariableIndex=0, value=[Name(name=hello)])])])]"),
                TraceEvent("HydratedFragment", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [DehydratedFragment(build=[BuildItem(index=0, key=test:t1)], patchInternal=[], patchDescendant=[PatchDescendantItem(declarationIndex=0, steps=[PatchDescendantStep(dependencyMask=1, stateVariableIndex=0, value=[Name(name=hello)])])])]"),
                TraceEvent("AdaptiveT1", 4, "before-Create", ""),
                TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [[Name(name=hello)]]"),
                TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [[Name(name=hello)]]"),
                TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [[Name(name=hello)]]"),
                TraceEvent("AdaptiveT1", 4, "after-Create", ""),
                TraceEvent("HydratedFragment", 3, "after-Create", ""),
                TraceEvent("<root>", 2, "after-Create", ""),
                TraceEvent("<root>", 2, "before-Mount", ""),
                TraceEvent("HydratedFragment", 3, "before-Mount", ""),
                TraceEvent("AdaptiveT1", 4, "before-Mount", ""),
                TraceEvent("AdaptiveT1", 4, "after-Mount", ""),
                TraceEvent("HydratedFragment", 3, "after-Mount", ""),
                TraceEvent("<root>", 2, "after-Mount", "")
            )))
            //@formatter:on
        }
    }
}