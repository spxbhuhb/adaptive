package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.grove.hydration.lfm.LfmConst
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.hydration.lfm.LfmMapping
import `fun`.adaptive.grove.sheet.model.DescendantInfo
import `fun`.adaptive.grove.sheet.model.SheetViewModel
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.utility.UUID

class Add(
    x : DPixel, y: DPixel, template : LfmDescendant
) : SheetOperation() {

    val fragment = newDescendant(x, y, template)

    override fun commit(viewModel: SheetViewModel) : Boolean {
        viewModel += fragment
        return false
    }

    override fun revert(viewModel: SheetViewModel) {
        viewModel -= fragment
    }

    fun newDescendant(x : DPixel, y : DPixel, template : LfmDescendant) : LfmDescendant {
        val templateInstructionMapping = template.mapping.first()
        val templateInstructions = templateInstructionMapping.mapping.value as AdaptiveInstructionGroup

        val uuid = UUID<LfmDescendant>()

        val instanceInstructions = instructionsOf(
            templateInstructions.removeAll { it is Position || it is DescendantInfo },
            DescendantInfo(uuid),
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

        return LfmDescendant(uuid, template.key, instanceMapping)
    }

    override fun toString(): String = "Add -- ${fragment.key} -- ${fragment.uuid}"

}