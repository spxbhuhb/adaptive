package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.sheet.control.refreshSelection
import `fun`.adaptive.grove.sheet.model.SheetViewModel
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.utility.UUID

/**
 * @property  start  The VM time when the move operation started. The operation uses this time to merge
 *                   with previous move operations.
 */
class Move(
    val start: Long,
    val deltaX: DPixel,
    val deltaY: DPixel
) : SheetOperation() {

    val undoData = mutableMapOf<UUID<LfmDescendant>, AdaptiveInstructionGroup>()

    var mergedDeltaX = deltaX
    var mergedDeltaY = deltaY

    override fun commit(viewModel: SheetViewModel): Boolean {
        val last = viewModel.engine.undoStack.lastOrNull()
        val merge = (last is Move && last.start == start)

        val effectiveDeltaX : DPixel
        val effectiveDeltaY : DPixel

        if (merge) {
            undoData.putAll(last.undoData)
            mergedDeltaX += last.mergedDeltaX
            mergedDeltaY += last.mergedDeltaY
            effectiveDeltaX = deltaX
            effectiveDeltaY = deltaY
        } else {
            effectiveDeltaX = mergedDeltaX
            effectiveDeltaY = mergedDeltaY
        }

        viewModel.forEachSelected { model, fragment ->

            val originalInstructions = fragment.instructions

            if (! merge) {
                undoData[model.uuid] = originalInstructions
            }

            // FIXME - mixed density dependent and density independent pixels
            val originalPosition = originalInstructions.firstInstanceOf<Position>()
            val newPosition = Position(originalPosition.top + effectiveDeltaY, originalPosition.left + effectiveDeltaX)

            val newInstructions = instructionsOf(
                originalInstructions.removeAll { it is Position },
                newPosition
            )

            fragment.setStateVariable(0, newInstructions)
            fragment.genPatchInternal()

            //model.update(newInstructions)
        }

//        viewModel.root.closePatchBatch()
        viewModel.refreshSelection()

        return merge
    }

    override fun revert(viewModel: SheetViewModel) {
        viewModel.forEachSelected { model, fragment ->
            val originalInstructions = undoData[model.uuid] ?: return@forEachSelected
            fragment.setStateVariable(0, originalInstructions)
            fragment.genPatchInternal()
            model.update(originalInstructions)
        }
//        viewModel.root.closePatchBatch()
        viewModel.refreshSelection()
    }

    override fun toString(): String = "Move -- mergedDeltaX=$mergedDeltaX  mergedDeltaY=$mergedDeltaY"


}