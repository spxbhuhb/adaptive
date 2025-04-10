package `fun`.adaptive.document.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup

@Adat
class DocStyle(
    val id : DocStyleId,
    val instructions: AdaptiveInstructionGroup,
    val name : String? = null
)