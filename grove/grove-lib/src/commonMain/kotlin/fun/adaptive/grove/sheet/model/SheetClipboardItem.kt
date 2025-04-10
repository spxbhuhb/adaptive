package `fun`.adaptive.grove.sheet.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.utility.UUID

@Adat
class SheetClipboardItem(
    val clipboardIndex : ClipboardIndex,
    val name : String,
    val model : UUID<LfmDescendant>,
    val instructions : AdaptiveInstructionGroup?,
    val group : ClipboardIndex?,
    val members : List<ClipboardIndex>?
)