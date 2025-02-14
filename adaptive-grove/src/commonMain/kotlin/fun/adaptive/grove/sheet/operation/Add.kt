package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.grove.sheet.model.ItemIndex
import `fun`.adaptive.grove.sheet.model.SheetSelection
import `fun`.adaptive.ui.instruction.DPixel

@Adat
class Add(
    val x: DPixel,
    val y: DPixel,
    val template: LfmDescendant
) : SheetOperation() {

    lateinit var originalSelection : SheetSelection
    var index = ItemIndex(-1)

    override fun commit(controller: SheetViewController): OperationResult {
        with (controller) {

            if (firstRun) {
                originalSelection = selection
                index = createItem(y, x, template).index
            }

            showItem(index)
            select(index)

            return OperationResult.PUSH

        }
    }

    override fun revert(controller : SheetViewController) {
        controller.hideItem(index)
        controller.select(originalSelection.items, additional = false)
    }

    override fun toString(): String = "Add"

}