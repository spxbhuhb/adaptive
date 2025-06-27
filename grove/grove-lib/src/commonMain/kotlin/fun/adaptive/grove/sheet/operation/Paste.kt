package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.grove.sheet.SheetViewBackend
import `fun`.adaptive.grove.sheet.model.SheetItem
import `fun`.adaptive.grove.sheet.model.SheetSelection
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.instruction.layout.Size

@Adat
class Paste : SheetOperation() {

    lateinit var originalSelection: SheetSelection
    val pastedItems = mutableListOf<SheetItem>()

    override fun commit(controller: SheetViewBackend): OperationResult {
        with(controller) {

            if (clipboard.isEmpty()) return OperationResult.DROP

            if (firstRun) {
                originalSelection = controller.selection

                val shift = (shift(controller) * 5.0)
                val indexMap = indexMapFor(clipboard.items)

                clipboard.items.forEach { clipboardItem ->
                    val position = clipboardItem.instructions?.lastInstanceOfOrNull<Position>() ?: Position.ZERO
                    val size = clipboardItem.instructions?.lastInstanceOfOrNull<Size>()
                    pastedItems += controller.createItem(clipboardItem, position + shift, size, indexMap)
                }
            }

            pastedItems.forEach { showItem(it) }

            controller.select(pastedItems, additional = false)

            return OperationResult.PUSH
        }
    }

    override fun revert(controller: SheetViewBackend) {
        pastedItems.forEach { controller.hideItem(it) }
        controller.select(originalSelection.items, additional = false)
    }

    override fun toString(): String =
        "Paste"

    private fun shift(controller: SheetViewBackend): Int {
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