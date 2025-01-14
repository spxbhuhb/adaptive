package `fun`.adaptive.grove.hydration.model

import `fun`.adaptive.adat.Adat

@Adat
class AfmStateVariable(
    val name : String,
    val signature : String,
    val index : Int,
    val dependencyMask : Int,
    val isExternal : Boolean,
    val value : Any?
)