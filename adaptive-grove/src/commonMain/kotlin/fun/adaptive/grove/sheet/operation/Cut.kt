package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.grove.sheet.model.SheetClipboard
import `fun`.adaptive.grove.sheet.model.SheetItem
import `fun`.adaptive.grove.sheet.model.SheetSelection

@Adat
class Cut : SheetOperation() {

    lateinit var originalClipboard: SheetClipboard
    lateinit var originalSelection : SheetSelection

    lateinit var cutData : SheetClipboard

    override fun commit(controller: SheetViewController): OperationResult {
        with(controller) {

            if (selection.isEmpty()) return OperationResult.DROP

            if (firstRun) {
                originalSelection = selection
                originalClipboard = clipboard
                cutData = selectionToClipboard()
            }

            originalSelection.items.forEach {
                hideItem(it)
            }

            clipboard = cutData

            select()

            return OperationResult.PUSH
        }
    }

    override fun revert(controller: SheetViewController) {
        with(controller) {
            originalSelection.items.forEach { showItem(it) }
            clipboard = originalClipboard
            select(originalSelection.items, additional = false)
        }
    }

    override fun toString(): String =
        "Cut"
}