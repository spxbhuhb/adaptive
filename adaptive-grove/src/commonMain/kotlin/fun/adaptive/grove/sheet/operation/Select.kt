package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.sheet.model.SelectionInfo
import `fun`.adaptive.grove.sheet.model.SheetSelection
import `fun`.adaptive.grove.sheet.model.SheetViewModel

class Select(
    val items: List<SelectionInfo>
) : SheetOperation() {

    val undoData = mutableListOf<SelectionInfo>()

    override fun commit(viewModel: SheetViewModel): Boolean {
        undoData += viewModel.selection.value.selected
        viewModel.selection.update(SheetSelection(items))
        return false
    }

    override fun revert(viewModel: SheetViewModel) {
        viewModel.selection.update(SheetSelection(undoData))
    }

    override fun toString(): String =
        "Select -- $items"
}