package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.grove.sheet.model.ItemIndex
import `fun`.adaptive.grove.sheet.model.SheetItem
import `fun`.adaptive.grove.sheet.model.SheetSelection.Companion.emptySelection

class Ungroup : SheetOperation() {

    var originalSelection = emptySelection
    var originalMembers = mutableMapOf<ItemIndex, List<ItemIndex>>()

    override fun commit(controller: SheetViewController): OperationResult {
        if (controller.selection.isEmpty()) {
            return OperationResult.DROP
        }

        if (firstRun) {
            originalSelection = controller.selection
        }

        val newSelection = mutableListOf<SheetItem>()

        for (selectedItem in originalSelection.items) {

            if (! selectedItem.isGroup) {
                newSelection += selectedItem
                continue
            }

            val members = selectedItem.members!!

            if (firstRun) originalMembers[selectedItem.index] = members

            members.forEach { index ->
                val groupItem = controller.items[index]
                groupItem.group = null
                newSelection += groupItem
            }

            selectedItem.members = emptyList()
            controller.hideItem(selectedItem.index)
        }

        controller.select(newSelection)

        return OperationResult.PUSH
    }

    override fun revert(controller: SheetViewController) {
        for (selectedItem in originalSelection.items) {
            if (! selectedItem.isGroup) continue

            val members = originalMembers[selectedItem.index]!!

            selectedItem.members = members

            members.forEach { index ->
                controller.items[index].group = selectedItem.index
            }

            controller.showItem(selectedItem.index)
        }

        controller.select(originalSelection.items)
    }

    override fun toString(): String =
        "Ungroup -- ${originalSelection.items.size} ${originalSelection.items.joinToString { "${it.index}:${it.model.key}" }}"
}