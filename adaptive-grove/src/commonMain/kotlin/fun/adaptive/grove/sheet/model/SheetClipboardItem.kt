package `fun`.adaptive.grove.sheet.model

import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant

class SheetClipboardItem(
    val index : ClipboardIndex,
    val name : String,
    val model : LfmDescendant,
    val instructions : AdaptiveInstructionGroup?,
    val group : ClipboardIndex?,
    val members : List<ClipboardIndex>?
)