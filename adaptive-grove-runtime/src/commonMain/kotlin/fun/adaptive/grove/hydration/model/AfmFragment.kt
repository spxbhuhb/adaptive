package `fun`.adaptive.grove.hydration.model

import `fun`.adaptive.adat.Adat

@Adat
class AfmFragment(
    val variables : List<AfmStateVariable>,
    val build : List<AfmBuildBranch>,
    val patchInternal : List<AfmPatchInternalBranch>,
    val patchDescendant : List<AfmPatchDescendantBranch>
)