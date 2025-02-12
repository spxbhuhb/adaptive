package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.grove.sheet.model.SheetItem
import `fun`.adaptive.grove.sheet.model.SheetSelection
import `fun`.adaptive.ui.instruction.dp

class Paste : SheetOperation() {

    lateinit var originalSelection : SheetSelection
    val items = mutableListOf<SheetItem>()

    override fun commit(controller: SheetViewController): OperationResult {
        if (controller.clipboard.models.isEmpty()) {
            return OperationResult.DROP
        }

        if (firstRun) {
            originalSelection = controller.selection
        }

        val models = controller.clipboard.models
        val frames = controller.clipboard.frames

        val shift = (shift(controller) * 5).dp

        models.forEachIndexed { modelIndex, model ->

            val frame = frames[modelIndex]

            // FIXME mix of device dependent and device independent pixels

            if (firstRun) {
                val item = controller.addItem(frame.left.dp + shift, frame.top.dp + shift, model)
                items += item
            } else {
                controller.showItem(items[modelIndex].index)
            }

        }

        controller.select(items)

        return OperationResult.PUSH
    }

    override fun revert(controller: SheetViewController) {
        items.forEach { controller.hideItem(it.index) }
        controller.select(originalSelection.items)
    }

    override fun toString(): String =
        "Paste"

    private fun shift(controller: SheetViewController) : Int {
        var pastesBefore = 0
        val stack = controller.undoStack

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