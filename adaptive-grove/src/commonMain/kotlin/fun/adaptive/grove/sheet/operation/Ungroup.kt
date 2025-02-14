package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.grove.sheet.model.ItemIndex
import `fun`.adaptive.grove.sheet.model.SheetItem
import `fun`.adaptive.grove.sheet.model.SheetSelection.Companion.emptySelection

@Adat
class Ungroup : SheetOperation() {

    var originalSelection = emptySelection
    var originalMembers = mutableMapOf<ItemIndex, MutableList<ItemIndex>?>()

    override fun commit(controller: SheetViewController): OperationResult {
        with (controller) {

            if (selection.isEmpty()) return OperationResult.DROP

            if (firstRun) {
                originalSelection = controller.selection
                originalSelection.items.forEach {
                    originalMembers[it.index] = it.members
                }
            }

            val newSelection = mutableListOf<SheetItem>()

            for (selectedItem in originalSelection.items) {

                if (! selectedItem.isGroup) {
                    newSelection += selectedItem
                    continue
                }

                val memberIndices = selectedItem.members ?: continue

                memberIndices.forEach { index ->
                    val member = items[index.value]
                    member.group = null
                    newSelection += member
                }

                selectedItem.members = mutableListOf()
                hideItem(selectedItem)
            }

            select(newSelection, additional = false)

            return OperationResult.PUSH
        }
    }

    override fun revert(controller: SheetViewController) {
        for (selectedItem in originalSelection.items) {
            if (! selectedItem.isGroup) continue

            val membersIndices = originalMembers[selectedItem.index] ?: continue

            selectedItem.members = membersIndices

            membersIndices.forEach { index ->
                controller.items[index.value].group = selectedItem.index
            }

            controller.showItem(selectedItem.index)
        }

        controller.select(originalSelection.items, additional = false)
    }

    override fun toString(): String =
        "Ungroup -- ${originalSelection.items.size} ${originalSelection.items.joinToString { "${it.index}:${it.model}" }}"
}