package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.sheet.model.SheetSelection
import `fun`.adaptive.grove.sheet.model.SheetViewModel
import `fun`.adaptive.ui.instruction.DPixel

class Add(
    val x: DPixel,
    val y: DPixel,
    val template: LfmDescendant
) : SheetOperation() {

    lateinit var originalSelection : SheetSelection
    var index = - 1

    override fun commit(viewModel: SheetViewModel): Boolean {

        if (firstRun) {
            originalSelection = viewModel.selection
            index = viewModel.nextIndex
            viewModel.addItem(index, x, y, template)
        } else {
            viewModel.showItem(index)
        }

        return false
    }

    override fun revert(viewModel: SheetViewModel) {
        viewModel.hideItem(index)
        viewModel.select(originalSelection.items)
    }

    override fun toString(): String = "Add"

}