package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.grove.sheet.model.SheetClipboard

class Copy : SheetOperation() {

    lateinit var copyData : SheetClipboard
    lateinit var undoData : SheetClipboard

    override fun commit(controller: SheetViewController): OperationResult {
        if (controller.selection.isEmpty()) {
            return OperationResult.DROP
        }

        if (firstRun) {
            undoData = controller.clipboard
            copyData = controller.selectionToClipboard()
        }

        controller.clipboard = copyData

        return OperationResult.PUSH
    }

    override fun revert(controller: SheetViewController) {
        controller.clipboard = undoData
    }

    override fun toString(): String =
        "Copy"
}