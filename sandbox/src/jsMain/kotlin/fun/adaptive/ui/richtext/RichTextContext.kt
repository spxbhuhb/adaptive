package `fun`.adaptive.ui.richtext

import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup

class RichTextContext(
    val instructionSets: List<AdaptiveInstructionGroup>,
    val theme: RichTextTheme = RichTextTheme.DEFAULT
)