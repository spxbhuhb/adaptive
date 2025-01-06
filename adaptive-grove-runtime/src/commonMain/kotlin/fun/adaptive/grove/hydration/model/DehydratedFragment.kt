package `fun`.adaptive.grove.hydration.model

import `fun`.adaptive.adat.Adat

@Adat
class DehydratedFragment(
    val build : List<BuildItem>,
    val patchInternal : List<PatchInternalItem>,
    val patchDescendant : List<PatchDescendantItem>
)