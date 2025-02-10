package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.grove.sheet.model.SheetSelection
import `fun`.adaptive.ui.instruction.DPixel

class Add(
    val x: DPixel,
    val y: DPixel,
    val template: LfmDescendant
) : SheetOperation() {

    lateinit var originalSelection : SheetSelection
    var index = - 1

    override fun commit(controller : SheetViewController): Boolean {

        if (firstRun) {
            originalSelection = controller.selection
            index = controller.addItem(x, y, template).index
        } else {
            controller.showItem(index)
        }

        return false
    }

    override fun revert(controller : SheetViewController) {
        controller.hideItem(index)
        controller.select(originalSelection.items)
    }

    override fun toString(): String = "Add"

}