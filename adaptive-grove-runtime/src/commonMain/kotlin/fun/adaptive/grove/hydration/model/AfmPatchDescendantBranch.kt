package `fun`.adaptive.grove.hydration.model

import `fun`.adaptive.adat.Adat

@Adat
class AfmPatchDescendantBranch(
    val declarationIndex : Int,
    val steps : List<AfmPatchDescendantBranchStep>
)