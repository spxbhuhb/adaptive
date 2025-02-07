package `fun`.adaptive.grove.sheet.model

import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.ui.fragment.layout.RawFrame

class SheetClipboard(
    val models : List<LfmDescendant>,
    val frames : List<RawFrame>
)