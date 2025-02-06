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
        viewModel.select(items)
        return false
    }

    override fun revert(viewModel: SheetViewModel) {
        viewModel.select(undoData.items)
    }

    override fun toString(): String =
        "Select -- ${items.size} ${items.joinToString { "${it.index}:${it.model.key}" }}"
}