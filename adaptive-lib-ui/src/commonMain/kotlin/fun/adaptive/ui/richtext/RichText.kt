package `fun`.adaptive.ui.richtext

import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.grove.hydration.lfm.LfmFragment

class RichText(
    val instructionSets: List<AdaptiveInstructionGroup>,
    val model : LfmFragment = LfmFragment.EMPTY,
    val theme: RichTextTheme = RichTextTheme.DEFAULT
)