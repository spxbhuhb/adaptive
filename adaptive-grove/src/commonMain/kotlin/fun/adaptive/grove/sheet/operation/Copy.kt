package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.sheet.model.SheetClipboard
import `fun`.adaptive.grove.sheet.model.SheetViewModel

class Copy : SheetOperation() {

    lateinit var copyData : SheetClipboard
    lateinit var undoData : SheetClipboard

    override fun commit(viewModel: SheetViewModel): Boolean {

        if (firstRun) {
            undoData = viewModel.clipboard
            copyData = viewModel.selectionToClipboard()
        }

        viewModel.clipboard = copyData

        return false
    }

    override fun revert(viewModel: SheetViewModel) {
        viewModel.clipboard = undoData
    }

    override fun toString(): String =
        "Copy"
}