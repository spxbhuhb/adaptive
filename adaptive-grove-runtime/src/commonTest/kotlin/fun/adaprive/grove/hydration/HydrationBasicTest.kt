package `fun`.adaprive.grove.hydration

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.foundation.testing.TraceEvent
import `fun`.adaptive.foundation.testing.test
import `fun`.adaptive.grove.api.GroveFragmentFactory
import `fun`.adaptive.grove.api.hydrated
import `fun`.adaptive.grove.hydration.model.AfmBuildBranch
import `fun`.adaptive.grove.hydration.model.AfmFragment
import `fun`.adaptive.grove.hydration.model.AfmPatchDescendantBranch
import `fun`.adaptive.grove.hydration.model.AfmPatchDescendantBranchStep
import `fun`.adaptive.grove.hydration.model.AfmStateVariable
import kotlin.test.Test
import kotlin.test.assertEquals

class HydrationBasicTest {

    @Test
    fun empty() {
        test {
            it.fragmentFactory += GroveFragmentFactory
            hydrated(AfmFragment(emptyList(), emptyList(), emptyList(), emptyList()))
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
                TraceEvent("HydratedFragment", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [AfmFragment(variables=[], build=[], patchInternal=[], patchDescendant=[])]"),
                TraceEvent("HydratedFragment", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [AfmFragment(variables=[], build=[], patchInternal=[], patchDescendant=[])]"),
                TraceEvent("HydratedFragment", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [AfmFragment(variables=[], build=[], patchInternal=[], patchDescendant=[])]"),
                TraceEvent("AdaptiveSequence", 4, "before-Create", ""),
                TraceEvent("AdaptiveSequence", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
                TraceEvent("AdaptiveSequence", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
                TraceEvent("AdaptiveSequence", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
                TraceEvent("AdaptiveSequence", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
                TraceEvent("AdaptiveSequence", 4, "after-Create", ""),
                TraceEvent("HydratedFragment", 3, "after-Create", ""),
                TraceEvent("<root>", 2, "after-Create", ""),
                TraceEvent("<root>", 2, "before-Mount", ""),
                TraceEvent("HydratedFragment", 3, "before-Mount", ""),
                TraceEvent("AdaptiveSequence", 4, "before-Mount", ""),
                TraceEvent("AdaptiveSequence", 4, "after-Mount", ""),
                TraceEvent("HydratedFragment", 3, "after-Mount", ""),
                TraceEvent("<root>", 2, "after-Mount", "")
            )))
            //@formatter:on
        }
    }

    @Test
    fun t0() {
        val childIndex = 1

        val dehydrated = AfmFragment(
            emptyList(),
            listOf(
                AfmBuildBranch(childIndex, "test:t0")
            ),
            emptyList(),
            emptyList()
        )

        test {
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
                TraceEvent("HydratedFragment", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [AfmFragment(variables=[], build=[AfmBuildBranch(index=1, key=test:t0)], patchInternal=[], patchDescendant=[])]"),
                TraceEvent("HydratedFragment", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [AfmFragment(variables=[], build=[AfmBuildBranch(index=1, key=test:t0)], patchInternal=[], patchDescendant=[])]"),
                TraceEvent("HydratedFragment", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [AfmFragment(variables=[], build=[AfmBuildBranch(index=1, key=test:t0)], patchInternal=[], patchDescendant=[])]"),
                TraceEvent("AdaptiveSequence", 4, "before-Create", ""),
                TraceEvent("AdaptiveSequence", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
                TraceEvent("AdaptiveSequence", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [1]"),
                TraceEvent("AdaptiveSequence", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [1]"),
                TraceEvent("AdaptiveT0", 5, "before-Create", ""),
                TraceEvent("AdaptiveT0", 5, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
                TraceEvent("AdaptiveT0", 5, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
                TraceEvent("AdaptiveT0", 5, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
                TraceEvent("AdaptiveT0", 5, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
                TraceEvent("AdaptiveT0", 5, "after-Create", ""),
                TraceEvent("AdaptiveSequence", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [1]"),
                TraceEvent("AdaptiveSequence", 4, "after-Create", ""),
                TraceEvent("HydratedFragment", 3, "after-Create", ""),
                TraceEvent("<root>", 2, "after-Create", ""),
                TraceEvent("<root>", 2, "before-Mount", ""),
                TraceEvent("HydratedFragment", 3, "before-Mount", ""),
                TraceEvent("AdaptiveSequence", 4, "before-Mount", ""),
                TraceEvent("AdaptiveT0", 5, "before-Mount", ""),
                TraceEvent("AdaptiveT0", 5, "after-Mount", ""),
                TraceEvent("AdaptiveSequence", 4, "after-Mount", ""),
                TraceEvent("HydratedFragment", 3, "after-Mount", ""),
                TraceEvent("<root>", 2, "after-Mount", "")
            )))
            //@formatter:on
        }
    }

    @Test
    fun t1() {
        val childIndex = 1

        val dehydrated = AfmFragment(
            emptyList(),
            listOf(
                AfmBuildBranch(childIndex, "test:t1")
            ),
            emptyList(),
            listOf(
                AfmPatchDescendantBranch(
                    childIndex,
                    listOf(
                        AfmPatchDescendantBranchStep(
                            dependencyMask = 1,
                            stateVariableIndex = 0,
                            value = 123
                        )
                    )
                )
            )
        )

        test {
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
                TraceEvent("HydratedFragment", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [AfmFragment(variables=[], build=[AfmBuildBranch(index=0, key=test:t1)], patchInternal=[], patchDescendant=[AfmPatchDescendantBranch(declarationIndex=0, steps=[AfmPatchDescendantBranchStep(dependencyMask=1, stateVariableIndex=0, value=123)])])]"),
                TraceEvent("HydratedFragment", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [AfmFragment(variables=[], build=[AfmBuildBranch(index=0, key=test:t1)], patchInternal=[], patchDescendant=[AfmPatchDescendantBranch(declarationIndex=0, steps=[AfmPatchDescendantBranchStep(dependencyMask=1, stateVariableIndex=0, value=123)])])]"),
                TraceEvent("HydratedFragment", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [AfmFragment(variables=[], build=[AfmBuildBranch(index=0, key=test:t1)], patchInternal=[], patchDescendant=[AfmPatchDescendantBranch(declarationIndex=0, steps=[AfmPatchDescendantBranchStep(dependencyMask=1, stateVariableIndex=0, value=123)])])]"),
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
        val childIndex = 1

        val dehydrated = AfmFragment(
            emptyList(),
            listOf(
                AfmBuildBranch(childIndex, "test:t1")
            ),
            emptyList(),
            listOf(
                AfmPatchDescendantBranch(
                    childIndex,
                    listOf(
                        AfmPatchDescendantBranchStep(
                            dependencyMask = 1,
                            stateVariableIndex = 0,
                            value = instructionsOf(name("hello"))
                        )
                    )
                )
            )
        )

        test {
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
                TraceEvent("HydratedFragment", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [AfmFragment(variables=[], build=[AfmBuildBranch(index=0, key=test:t1)], patchInternal=[], patchDescendant=[AfmPatchDescendantBranch(declarationIndex=0, steps=[AfmPatchDescendantBranchStep(dependencyMask=1, stateVariableIndex=0, value=[Name(name=hello)])])])]"),
                TraceEvent("HydratedFragment", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [AfmFragment(variables=[], build=[AfmBuildBranch(index=0, key=test:t1)], patchInternal=[], patchDescendant=[AfmPatchDescendantBranch(declarationIndex=0, steps=[AfmPatchDescendantBranchStep(dependencyMask=1, stateVariableIndex=0, value=[Name(name=hello)])])])]"),
                TraceEvent("HydratedFragment", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [AfmFragment(variables=[], build=[AfmBuildBranch(index=0, key=test:t1)], patchInternal=[], patchDescendant=[AfmPatchDescendantBranch(declarationIndex=0, steps=[AfmPatchDescendantBranchStep(dependencyMask=1, stateVariableIndex=0, value=[Name(name=hello)])])])]"),
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

    @Test
    fun state() {
        test(printTrace = true) {
            it.fragmentFactory += GroveFragmentFactory
            hydrated(
                AfmFragment(
                    listOf(
                        AfmStateVariable("e", "I", 1, 0, isExternal = true, value = null),
                        AfmStateVariable("i", "I", 2, 0, isExternal = false, value = 12)
                    ),
                    emptyList(), emptyList(), emptyList()
                )
            )

        }.also {
            //@formatter:off
            assertEquals("OK", it.assert(listOf(

            )))
            //@formatter:on
        }
    }
}