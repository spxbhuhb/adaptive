package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.grove.hydration.lfm.LfmConst
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.hydration.lfm.LfmMapping
import `fun`.adaptive.grove.sheet.model.ItemInfo
import `fun`.adaptive.grove.sheet.model.SheetItem
import `fun`.adaptive.grove.sheet.model.SheetViewModel
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.utility.UUID

class Add(
    val x: DPixel,
    val y: DPixel,
    val template: LfmDescendant
) : SheetOperation() {

    var index = - 1

    override fun commit(viewModel: SheetViewModel): Boolean {
        if (index == -1) {
            index = viewModel.nextIndex
            viewModel += newItem(index, x, y, template)
        } else {
            viewModel += viewModel.items[index]
        }
        return false
    }

    override fun revert(viewModel: SheetViewModel) {
        viewModel -= index
    }

    fun newItem(index: Int, x: DPixel, y: DPixel, template: LfmDescendant): SheetItem {

        val templateInstructions = template.instructions

        val instanceInstructions = instructionsOf(
            templateInstructions.removeAll { it is Position || it is ItemInfo },
            ItemInfo(index),
            Position(y, x)
        )

        val instanceInstructionMapping =
            LfmMapping(
                dependencyMask = 0,
                mapping = LfmConst(
                    typeSignature<AdaptiveInstructionGroup>(),
                    instanceInstructions
                )
            )

        val instanceMapping = listOf(instanceInstructionMapping) + template.mapping.drop(1)

        return SheetItem(index, LfmDescendant(UUID(), template.key, instanceMapping))

    }

    override fun toString(): String = "Add"

}