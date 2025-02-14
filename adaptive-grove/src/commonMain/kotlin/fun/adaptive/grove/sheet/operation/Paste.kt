package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.grove.sheet.model.SheetItem
import `fun`.adaptive.grove.sheet.model.SheetSelection
import `fun`.adaptive.ui.instruction.layout.Position

@Adat
class Paste : SheetOperation() {

    lateinit var originalSelection: SheetSelection
    val pastedItems = mutableListOf<SheetItem>()

    override fun commit(controller: SheetViewController): OperationResult {
        with(controller) {

            if (clipboard.isEmpty()) return OperationResult.DROP

            if (firstRun) {
                originalSelection = controller.selection

                val shift = (shift(controller) * 5.0)
                val indexMap = indexMapFor(clipboard.items)

                clipboard.items.forEach { clipboardItem ->
                    val position = clipboardItem.instructions?.lastInstanceOfOrNull<Position>() ?: Position.ZERO
                    pastedItems += controller.createItem(clipboardItem, position + shift, indexMap)
                }
            }

            pastedItems.forEach { showItem(it) }

            controller.select(pastedItems, additional = false)

            return OperationResult.PUSH
        }
    }

    override fun revert(controller: SheetViewController) {
        pastedItems.forEach { controller.hideItem(it) }
        controller.select(originalSelection.items, additional = false)
    }

    override fun toString(): String =
        "Paste"

    private fun shift(controller: SheetViewController): Int {
        var pastesBefore = 0
        val stack = controller.undoStack

        for (i in stack.size - 1 downTo 0) {
            if (stack[i] is Paste) {
                pastesBefore ++
            } else {
                break
            }
        }

        return pastesBefore + 1
    }
}