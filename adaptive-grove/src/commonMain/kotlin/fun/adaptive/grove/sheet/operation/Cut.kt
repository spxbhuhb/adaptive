package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.sheet.model.SheetClipboard
import `fun`.adaptive.grove.sheet.model.SheetSelection
import `fun`.adaptive.grove.sheet.model.SheetViewModel

class Cut : SheetOperation() {

    lateinit var undoData: SheetClipboard

    override fun commit(viewModel: SheetViewModel): Boolean {

        val copyData = mutableListOf<LfmDescendant>()

        viewModel.forEachSelected { descendant, _ ->
            copyData += descendant
            viewModel -= descendant
        }

        undoData = viewModel.clipboard

        viewModel.clipboard = SheetClipboard(copyData)
        viewModel.selection.update(SheetSelection(emptyList()))

        return false
    }

    override fun revert(viewModel: SheetViewModel) {
        viewModel.clipboard = undoData
    }

    override fun toString(): String =
        "Cut"
}