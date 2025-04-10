package `fun`.adaptive.grove.hydration.lfm

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.internal.StateVariableMask
import `fun`.adaptive.reflect.typeSignature

@Adat
class LfmMapping(
    val dependencyMask: StateVariableMask,
    val mapping : LfmConst
)