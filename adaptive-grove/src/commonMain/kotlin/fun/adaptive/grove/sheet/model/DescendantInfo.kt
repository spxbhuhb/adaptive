package `fun`.adaptive.grove.sheet.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.utility.UUID

@Adat
class DescendantInfo(
    val uuid : UUID<LfmDescendant>
) : AdaptiveInstruction
