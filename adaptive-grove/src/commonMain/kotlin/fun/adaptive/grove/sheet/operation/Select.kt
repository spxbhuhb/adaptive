package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.sheet.model.SheetItem
import `fun`.adaptive.grove.sheet.model.SheetSelection
import `fun`.adaptive.grove.sheet.model.SheetViewModel

class Select(
    val items: List<SheetItem>
) : SheetOperation() {

    var undoData = SheetSelection(emptyList())

    override fun commit(viewModel: SheetViewModel): Boolean {
        undoData = viewModel.selection
        viewModel.selection = SheetSelection(items)
        return false
    }

    override fun revert(viewModel: SheetViewModel) {
        viewModel.selection = undoData
    }

    override fun toString(): String =
        "Select -- $items"
}