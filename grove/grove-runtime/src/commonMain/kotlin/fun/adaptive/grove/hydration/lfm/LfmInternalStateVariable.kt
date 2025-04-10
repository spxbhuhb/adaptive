package `fun`.adaptive.grove.hydration.lfm

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.internal.StateVariableMask

@Adat
class LfmInternalStateVariable(
    val name : String,
    val signature : String,
    val dependencyMask: StateVariableMask,
    val calculation : LfmExpression
)