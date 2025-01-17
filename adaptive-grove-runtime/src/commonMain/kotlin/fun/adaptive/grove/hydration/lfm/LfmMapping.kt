package `fun`.adaptive.grove.hydration.lfm

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.internal.StateVariableMask

@Adat
class LfmMapping(
    val dependencyMask: StateVariableMask,
    val mapping : LfmConst
)