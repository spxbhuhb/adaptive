package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.grove.sheet.model.SheetClipboard
import `fun`.adaptive.grove.sheet.model.SheetItem
import `fun`.adaptive.grove.sheet.model.SheetSelection

class Cut : SheetOperation() {

    lateinit var originalClipboard: SheetClipboard
    lateinit var originalSelection : SheetSelection

    lateinit var cutData : SheetClipboard

    val cutItems = mutableListOf<SheetItem>()

    override fun commit(controller: SheetViewController): Boolean {

        with(controller) {
            if (firstRun) {
                originalSelection = selection
                originalClipboard = clipboard
                cutData = selectionToClipboard()
            }

            forSelection {
                hideItem(it.index)
                cutItems += it
            }

            clipboard = cutData
            select()
        }

        return false
    }

    override fun revert(controller: SheetViewController) {
        with(controller) {
            cutItems.forEach { showItem(it.index) }
            clipboard = originalClipboard
            select(originalSelection.items)
        }
    }

    override fun toString(): String =
        "Cut"
}