package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.sheet.SheetViewBackend

@Adat
class AddModel(
    val model: LfmDescendant
) : SheetOperation() {

    override fun commit(controller: SheetViewBackend): OperationResult {
        if (model.uuid in controller.models) {
            return OperationResult.DROP
        }

        controller.models[model.uuid] = model

        return OperationResult.PUSH
    }

    override fun revert(controller : SheetViewBackend) {
        controller.models.remove(model.uuid)
    }

    override fun toString(): String = "AddModel $model"

}