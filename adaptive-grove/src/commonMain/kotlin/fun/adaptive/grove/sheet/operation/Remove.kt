package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.sheet.model.SheetItem
import `fun`.adaptive.grove.sheet.model.SheetViewModel

class Remove : SheetOperation() {

    val items = mutableListOf<SheetItem>()

    override fun commit(viewModel: SheetViewModel): Boolean {

        if (firstRun) {
            viewModel.forSelection { items += it }
        }

        viewModel.forSelection {
            viewModel.hideItem(it.index)
        }

        viewModel.select()

        return false
    }

    override fun revert(viewModel: SheetViewModel) {
        items.forEach { viewModel.showItem(it.index) }
        viewModel.select(items)
    }

    override fun toString(): String =
        "Remove"
}