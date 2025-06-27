package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.grove.sheet.SheetViewBackend
import `fun`.adaptive.grove.sheet.model.SheetSelection

@Adat
class Remove : SheetOperation() {

    lateinit var originalSelection: SheetSelection

    override fun commit(controller: SheetViewBackend): OperationResult {
        with(controller) {

            if (selection.isEmpty()) return OperationResult.DROP

            if (firstRun) {
                originalSelection = selection
            }

            originalSelection.items.forEach {
                hideItem(it)
            }

            select()

            return OperationResult.PUSH
        }
    }

    override fun revert(controller: SheetViewBackend) {
        originalSelection.items.forEach {
            controller.showItem(it)
        }
        controller.select(originalSelection.items, additional = false)
    }

    override fun toString(): String =
        "Remove"
}