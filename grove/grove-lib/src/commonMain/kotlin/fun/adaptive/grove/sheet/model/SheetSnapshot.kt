package `fun`.adaptive.grove.sheet.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.sheet.operation.SheetOperation

@Adat
class SheetSnapshot(
    val models : List<LfmDescendant>,
    val items : List<SheetClipboardItem>,
    val operations : List<SheetOperation>
)