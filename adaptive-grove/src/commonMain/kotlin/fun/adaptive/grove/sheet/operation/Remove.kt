package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.grove.sheet.model.SheetItem

class Remove : SheetOperation() {

    val items = mutableListOf<SheetItem>()

    override fun commit(controller: SheetViewController): OperationResult {
        if (controller.selection.isEmpty()) {
            return OperationResult.DROP
        }

        if (firstRun) {
            controller.forSelection { items += it }
        }

        controller.forSelection {
            controller.hideItem(it.index)
        }

        controller.select()

        return OperationResult.PUSH
    }

    override fun revert(controller: SheetViewController) {
        items.forEach { controller.showItem(it.index) }
        controller.select(items)
    }

    override fun toString(): String =
        "Remove"
}