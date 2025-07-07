package `fun`.adaptive.ui.fragment.paragraph.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup

@Adat
class ParagraphViewBackend(
    val instructionSets: List<AdaptiveInstructionGroup>,
    val items: List<ParagraphItem>
)