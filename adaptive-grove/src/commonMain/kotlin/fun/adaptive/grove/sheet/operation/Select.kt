package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.sheet.SelectionInfo
import `fun`.adaptive.grove.sheet.SheetSelection
import `fun`.adaptive.grove.sheet.SheetViewModel

class Select(
    val items: MutableList<SelectionInfo>
) : SheetOperation() {

    override fun applyTo(viewModel: SheetViewModel) {
        viewModel.selection.update(SheetSelection(items))
    }

    override fun toString(): String =
        "Select -- $items"
}