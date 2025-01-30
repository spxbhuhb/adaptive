package `fun`.adaptive.grove.sheet.control

import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.grove.hydration.lfm.LfmConst
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.hydration.lfm.LfmMapping
import `fun`.adaptive.grove.sheet.model.DescendantInfo
import `fun`.adaptive.grove.sheet.model.SheetViewModel
import `fun`.adaptive.grove.sheet.operation.Add
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.utility.UUID

fun SheetViewModel.add(x : Double, y: Double, template : LfmDescendant) {

    val templateInstructionMapping = template.mapping.first()
    val templateInstructions = templateInstructionMapping.mapping.value as AdaptiveInstructionGroup

    val uuid = UUID<LfmDescendant>()

    val instanceInstructions = instructionsOf(
        templateInstructions,
        DescendantInfo(uuid),
        Position(y.dp, x.dp)
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

    this += Add(uuid, template.key, instanceMapping)
}