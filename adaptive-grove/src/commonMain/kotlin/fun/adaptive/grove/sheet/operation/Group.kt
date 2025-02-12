package `fun`.adaptive.grove.sheet.operation

import adaptive_grove.generated.resources.group
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.grove.sheet.model.ItemIndex
import `fun`.adaptive.grove.sheet.model.SheetSelection.Companion.emptySelection
import `fun`.adaptive.resource.string.Strings

class Group : SheetOperation() {

    var originalSelection = emptySelection
    var originalGroups = mutableMapOf<ItemIndex, ItemIndex?>() // item, group
    var index = - 1

    override fun commit(controller: SheetViewController): OperationResult {
        if (controller.selection.isEmpty()) {
            return OperationResult.DROP
        }

        if (firstRun) {
            originalSelection = controller.selection
            originalSelection.forItems { originalGroups[it.index] = it.group }
            val frame = controller.selectionFrame
            val group = LfmDescendant("aui:rectangle", instructionsOf(frame.size))
            val item = controller.addItem(frame.left, frame.top, group)
            index = item.index

            item.name = Strings.group
            item.members = originalSelection.mapItems { it.index }
        }

        controller.forSelection {
            it.group = index
        }

        controller.select(index)

        return OperationResult.PUSH
    }

    override fun revert(controller: SheetViewController) {
        originalSelection.items.forEach { it.group = originalGroups[it.index] }
        controller.select(originalSelection.items)
    }

    override fun toString(): String =
        "Group -- ${originalSelection.items.size} ${originalSelection.items.joinToString { "${it.index}:${it.model.key}" }}"
}