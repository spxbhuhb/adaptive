package `fun`.adaptive.grove.sheet.operation

import adaptive_grove.generated.resources.group
import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.adaptive
import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.grove.sheet.model.ItemIndex
import `fun`.adaptive.grove.sheet.model.SheetSelection
import `fun`.adaptive.resource.string.Strings

@Adat
class Group : SheetOperation() {

    lateinit var originalSelection : SheetSelection
    var originalGroups = mutableMapOf<ItemIndex, ItemIndex?>() // item, group
    var index = ItemIndex(-1)

    override fun commit(controller: SheetViewController): OperationResult {
        with (controller) {
            if (selection.isEmpty()) return OperationResult.DROP

            if (firstRun) {
                originalSelection = selection
                originalSelection.forItems { originalGroups[it.index] = it.group }

                val frame = selectionFrame
                val model = groupItemModel(frame.size)
                val item = createItem(frame.top, frame.left, model)

                index = item.index

                item.name = Strings.group
                item.members = originalSelection.mapItems { it.index }.toMutableList()
            }

            showItem(index)

            originalSelection.forItems {
                it.group = index
            }

            controller.select(index)

            return OperationResult.PUSH
        }
    }

    override fun revert(controller: SheetViewController) {
        controller.hideItem(index)
        originalSelection.items.forEach { it.group = originalGroups[it.index] }
        controller.select(originalSelection.items, additional = false)
    }

    override fun toString(): String =
        "Group -- ${originalSelection.items.size} ${originalSelection.items.joinToString { "${it.index}:${it.model.key}" }}"
}