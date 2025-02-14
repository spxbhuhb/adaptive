package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.grove.sheet.model.SheetItem
import `fun`.adaptive.ui.instruction.layout.Position

@Adat
class SelectByPosition(
    val position: Position,
    override val additional: Boolean
) : Select() {

    override fun SheetViewController.findItems() : List<SheetItem> {
        return findByRenderData { it.contains(position.left.px, position.top.px) }
    }

    override fun toString(): String =
        "Select by position $position $traceString"
}