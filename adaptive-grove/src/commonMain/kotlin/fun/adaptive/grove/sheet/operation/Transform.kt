package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.ui.fragment.layout.RawFrame
import `fun`.adaptive.ui.instruction.DPixel

/**
 * @property  start  The VM time when the move operation started. The operation uses this time to merge
 *                   with previous move operations.
 */
abstract class Transform(
    val start: Long,
    val deltaX: DPixel,
    val deltaY: DPixel
) : SheetOperation() {

    val undoData = mutableMapOf<Int, AdaptiveInstructionGroup>()

    var mergedDeltaX = deltaX
    var mergedDeltaY = deltaY

    override fun commit(controller: SheetViewController): Boolean {
        val last = controller.undoStack.lastOrNull()
        val merge = (last is Transform && last.start == start)

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

        controller.forSelection { item ->

            val fragment = item.fragment
            val originalInstructions = fragment.instructions

            // The selection cannot change when we merge with the previous move.
            // Thus, we have to add the original instructions only when this is the
            // first move of a merged move series.

            if (! merge) {
                undoData[item.index] = originalInstructions
            }

            fragment.setStateVariable(0, newInstructions(originalInstructions, effectiveDeltaX, effectiveDeltaY))
            fragment.genPatchInternal()
        }

        controller.select(
            controller.selection.items,
            newFrame(controller, effectiveDeltaX, effectiveDeltaY)
        )

        return merge
    }

    abstract fun newFrame(
        controller: SheetViewController,
        effectiveDeltaX: DPixel,
        effectiveDeltaY: DPixel
    ): RawFrame

    abstract fun newInstructions(
        originalInstructions: AdaptiveInstructionGroup,
        effectiveDeltaX: DPixel,
        effectiveDeltaY: DPixel
    ): AdaptiveInstructionGroup

    override fun revert(controller: SheetViewController) {
        controller.forSelection { item ->
            val originalInstructions = undoData[item.index] ?: return@forSelection
            val fragment = item.fragment
            fragment.setStateVariable(0, originalInstructions)
            fragment.genPatchInternal()
            controller.updateLayout(item)
        }
        controller.select(controller.selection.items)
    }

}