package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.sheet.model.SheetClipboard
import `fun`.adaptive.grove.sheet.model.SheetItem
import `fun`.adaptive.grove.sheet.model.SheetViewModel

class Cut : SheetOperation() {

    lateinit var originalClipboard: SheetClipboard
    val items = mutableListOf<SheetItem>()

    override fun commit(viewModel: SheetViewModel): Boolean {

        val copyData = mutableListOf<LfmDescendant>()

        viewModel.forSelection {
            copyData += it.model
            viewModel -= it.index
            items += it
        }

        originalClipboard = viewModel.clipboard

        viewModel.clipboard = SheetClipboard(copyData)
        viewModel.select()

        return false
    }

    override fun revert(viewModel: SheetViewModel) {
        items.forEach { viewModel += it }
        viewModel.clipboard = originalClipboard
    }

    override fun toString(): String =
        "Cut"
}