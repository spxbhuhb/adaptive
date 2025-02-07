package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.sheet.model.SheetItem
import `fun`.adaptive.grove.sheet.model.SheetSelection
import `fun`.adaptive.grove.sheet.model.SheetViewModel
import `fun`.adaptive.ui.instruction.dp

class Paste : SheetOperation() {

    lateinit var originalSelection : SheetSelection
    val items = mutableListOf<SheetItem>()

    override fun commit(viewModel: SheetViewModel): Boolean {

        if (firstRun) {
            originalSelection = viewModel.selection
        }

        val models = viewModel.clipboard.models
        val frames = viewModel.clipboard.frames

        val shift = (shift(viewModel) * 10).dp

        models.forEachIndexed { modelIndex, model ->

            val frame = frames[modelIndex]

            // FIXME mix of device dependent and device independent pixels

            if (firstRun) {
                val item = viewModel.addItem(viewModel.nextIndex, frame.left.dp + shift, frame.top.dp + shift, model)
                items += item
            } else {
                viewModel.showItem(items[modelIndex].index)
            }

        }

        viewModel.select(items)

        return false
    }

    override fun revert(viewModel: SheetViewModel) {
        items.forEach { viewModel.hideItem(it.index) }
        viewModel.select(originalSelection.items)
    }

    override fun toString(): String =
        "Paste"

    private fun shift(viewModel: SheetViewModel) : Int {
        var pastesBefore = 0
        val stack = viewModel.engine.undoStack

        for (i in stack.size - 1 downTo 0) {
            if (stack[i] is Paste) {
                pastesBefore++
            } else {
                break
            }
        }

        return pastesBefore + 1
    }
}