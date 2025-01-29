package `fun`.adaptive.grove.ufd

import `fun`.adaptive.auto.api.autoCollectionOrigin
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.grove.hydration.lfm.LfmConst
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.hydration.lfm.LfmMapping
import `fun`.adaptive.grove.sheet.DescendantInfo
import `fun`.adaptive.grove.sheet.SheetViewModel
import `fun`.adaptive.grove.sheet.operation.Add
import `fun`.adaptive.grove.sheet.operation.Move
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.event.UIEvent
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.utility.UUID

class UfdContext(
    val sheetViewModel: SheetViewModel
) {

    val palette = autoCollectionOrigin(
        listOf(
            descendant("aui:text", emptyInstructions, LfmMapping(dependencyMask = 0, LfmConst("T", "Text"))),
            descendant("aui:box", instructionsOf(size(100.dp, 100.dp), border(colors.danger, 1.dp)))
        )
    )

    fun select(x : Double, y : Double) {
        sheetViewModel.select(x, y)
    }

    fun addDescendant(event: UIEvent) {
        val transfer = (event.transferData?.data as? LfmDescendant) ?: return
        val template = palette.firstOrNull { it.key == transfer.key } ?: return

        val templateInstructionMapping = template.mapping.first()
        val templateInstructions = templateInstructionMapping.mapping.value as AdaptiveInstructionGroup

        val uuid = UUID<LfmDescendant>()

        val instanceInstructions = instructionsOf(
            templateInstructions,
            DescendantInfo(uuid),
            Position(event.y.dp, event.x.dp)
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

        sheetViewModel += Add(uuid, template.key, instanceMapping)
    }

    fun descendant(key: String, instructions: AdaptiveInstructionGroup, vararg args: LfmMapping) =
        LfmDescendant(
            UUID(),
            key,
            listOf(
                LfmMapping(
                    dependencyMask = 0,
                    LfmConst(
                        typeSignature<AdaptiveInstructionGroup>(),
                        instructions
                    )
                ),
                *args
            )
        )

    fun move(moveStart : Long, deltaX: DPixel, deltaY: DPixel) {
        sheetViewModel += Move(moveStart, deltaX, deltaY)
    }
}