package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.grove.sheet.SheetViewBackend
import `fun`.adaptive.grove.sheet.model.SheetClipboard

@Adat
class Copy : SheetOperation() {

    lateinit var originalClipboard: SheetClipboard
    lateinit var copyData: SheetClipboard

    override fun commit(controller: SheetViewBackend): OperationResult {
        with(controller) {

            if (selection.isEmpty()) return OperationResult.DROP

            if (firstRun) {
                originalClipboard = clipboard
                copyData = selectionToClipboard()
            }

            clipboard = copyData

            return OperationResult.PUSH

        }
    }

    override fun revert(controller: SheetViewBackend) {
        controller.clipboard = originalClipboard
    }

    override fun toString(): String =
        "Copy -- ${copyData.items.size} items"
}