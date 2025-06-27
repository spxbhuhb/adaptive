package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.sheet.SheetViewBackend
import `fun`.adaptive.grove.sheet.model.ItemIndex
import `fun`.adaptive.grove.sheet.model.SheetSelection
import `fun`.adaptive.ui.api.position
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.utility.UUID

@Adat
class Add(
    val x: DPixel,
    val y: DPixel,
    val model: UUID<LfmDescendant>
) : SheetOperation() {

    lateinit var originalSelection : SheetSelection
    var index = ItemIndex(-1)

    override fun commit(controller: SheetViewBackend): OperationResult {
        with (controller) {

            if (firstRun) {
                originalSelection = selection
                index = createItem(model, position(y, x)).index
            }

            showItem(index)
            select(index)

            return OperationResult.PUSH

        }
    }

    override fun revert(controller : SheetViewBackend) {
        controller.hideItem(index)
        controller.select(originalSelection.items, additional = false)
    }

    override fun toString(): String = "Add"

}