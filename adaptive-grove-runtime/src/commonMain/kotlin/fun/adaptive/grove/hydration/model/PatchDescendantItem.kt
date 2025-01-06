package `fun`.adaptive.grove.hydration.model

import `fun`.adaptive.adat.Adat

@Adat
class PatchDescendantItem(
    val declarationIndex : Int,
    val steps : List<PatchDescendantStep>
)