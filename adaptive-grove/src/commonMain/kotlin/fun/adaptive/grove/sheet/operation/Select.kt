package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.grove.sheet.model.SheetItem
import `fun`.adaptive.grove.sheet.model.SheetSelection

abstract class Select: SheetOperation() {

    abstract val additional: Boolean

    lateinit var originalSelection: SheetSelection
    lateinit var selectedItems: List<SheetItem>

    override fun commit(controller: SheetViewController): OperationResult {
        with(controller) {

            if (firstRun) {
                originalSelection = selection
                selectedItems = findItems()
            }

            select(selectedItems, additional)

            return OperationResult.PUSH
        }
    }

    override fun revert(controller: SheetViewController) {
        controller.select(originalSelection.items, additional = false)
    }

    abstract fun SheetViewController.findItems(): List<SheetItem>

    val traceString: String
        get() = "additional: $additional size:${selectedItems.size} ${selectedItems.joinToString { "${it.index}:${it.model.key}" }}"

}