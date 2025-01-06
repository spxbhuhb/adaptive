package `fun`.adaptive.grove.hydration.model

import `fun`.adaptive.adat.Adat

@Adat
class PatchDescendantStep(
    val dependencyMask : Int,
    val stateVariableIndex : Int,
    val value : Any?
)