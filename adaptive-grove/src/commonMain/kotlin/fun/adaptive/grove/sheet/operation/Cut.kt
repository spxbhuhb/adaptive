package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.sheet.model.SheetClipboard
import `fun`.adaptive.grove.sheet.model.SheetItem
import `fun`.adaptive.grove.sheet.model.SheetSelection
import `fun`.adaptive.grove.sheet.model.SheetViewModel

class Cut : SheetOperation() {

    lateinit var originalClipboard: SheetClipboard
    lateinit var originalSelection : SheetSelection

    lateinit var cutData : SheetClipboard

    val items = mutableListOf<SheetItem>()

    override fun commit(viewModel: SheetViewModel): Boolean {

        if (firstRun) {
            originalSelection = viewModel.selection
            originalClipboard = viewModel.clipboard
            cutData = viewModel.selectionToClipboard()
        }

        viewModel.forSelection {
            viewModel.hideItem(it.index)
        }

        viewModel.clipboard = cutData
        viewModel.select()

        return false
    }

    override fun revert(viewModel: SheetViewModel) {
        items.forEach { viewModel.showItem(it.index) }
        viewModel.clipboard = originalClipboard
        viewModel.select(originalSelection.items)
    }

    override fun toString(): String =
        "Cut"
}