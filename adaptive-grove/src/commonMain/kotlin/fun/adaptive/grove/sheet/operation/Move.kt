package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.grove.sheet.model.SheetViewModel
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.layout.Position

/**
 * @property  start  The VM time when the move operation started. The operation uses this time to merge
 *                   with previous move operations.
 */
class Move(
    val start: Long,
    val deltaX: DPixel,
    val deltaY: DPixel
) : SheetOperation() {

    val undoData = mutableMapOf<Int, AdaptiveInstructionGroup>()

    var mergedDeltaX = deltaX
    var mergedDeltaY = deltaY

    override fun commit(viewModel: SheetViewModel): Boolean {
        val last = viewModel.engine.undoStack.lastOrNull()
        val merge = (last is Move && last.start == start)

        val effectiveDeltaX: DPixel
        val effectiveDeltaY: DPixel

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

        viewModel.forSelection { item ->

            val fragment = item.fragment
            val originalInstructions = fragment.instructions

            // The selection cannot change when we merge with the previous move.
            // Thus, we have to add the original instructions only when this is the
            // first move of a merged move series.

            if (! merge) {
                undoData[item.index] = originalInstructions
            }

            // FIXME - mixed density dependent and density independent pixels
            val originalPosition = originalInstructions.firstInstanceOf<Position>()
            val newPosition = Position(originalPosition.top + effectiveDeltaY, originalPosition.left + effectiveDeltaX)

            val newInstructions = originalInstructions.removeAll { it is Position } + newPosition

            fragment.setStateVariable(0, newInstructions)
            fragment.genPatchInternal()
        }

        val newFrame = viewModel.selection.containingFrame.move(effectiveDeltaX.value, effectiveDeltaY.value)
        viewModel.select(viewModel.selection.items, newFrame)

        return merge
    }

    override fun revert(viewModel: SheetViewModel) {
        viewModel.forSelection { item ->
            val originalInstructions = undoData[item.index] ?: return@forSelection
            val fragment = item.fragment
            fragment.setStateVariable(0, originalInstructions)
            fragment.genPatchInternal()
            viewModel.drawingLayer.updateLayout(item)
        }
        viewModel.select(viewModel.selection.items)
    }

    override fun toString(): String = "Move -- mergedDeltaX=$mergedDeltaX  mergedDeltaY=$mergedDeltaY"


}