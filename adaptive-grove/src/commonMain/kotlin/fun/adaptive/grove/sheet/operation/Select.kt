package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.grove.sheet.model.SheetItem
import `fun`.adaptive.grove.sheet.model.SheetSelection

class Select(
    val items: List<SheetItem>
) : SheetOperation() {

    var undoData = SheetSelection(emptyList())

    override fun commit(controller: SheetViewController): Boolean {
        if (firstRun) {
            undoData = controller.selection
        }
        controller.select(items)
        return false
    }

    override fun revert(controller: SheetViewController) {
        controller.select(undoData.items)
    }

    override fun toString(): String =
        "Select -- ${items.size} ${items.joinToString { "${it.index}:${it.model.key}" }}"
}