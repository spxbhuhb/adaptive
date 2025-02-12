package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.grove.sheet.model.SheetSelection

class Group : SheetOperation() {

    var undoData = SheetSelection(emptyList())

    override fun commit(controller: SheetViewController): OperationResult {
        if (controller.selection.isEmpty()) {
            return OperationResult.DROP
        }

        if (firstRun) {
            undoData = controller.selection
        }

        val frame = controller.controlFrame
        //controller.addItem(frame.top, frame.left, )

        controller.forSelection {
        }


        return OperationResult.PUSH
    }

    override fun revert(controller: SheetViewController) {
        controller.select(undoData.items)
    }

    override fun toString(): String =
        "Group -- ${undoData.items.size} ${undoData.items.joinToString { "${it.index}:${it.model.key}" }}"
}