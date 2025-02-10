package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.ui.fragment.layout.RawFrame
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.layout.Position

/**
 * @property  start  The VM time when the move operation started. The operation uses this time to merge
 *                   with previous move operations.
 */
open class Move(
    start: Long,
    deltaX: DPixel,
    deltaY: DPixel
) : Transform(start, deltaX, deltaY) {

    override fun newFrame(
        controller: SheetViewController,
        effectiveDeltaX: DPixel,
        effectiveDeltaY: DPixel
    ): RawFrame =
        controller.selection.containingFrame.move(effectiveDeltaX.value, effectiveDeltaY.value)

    override fun newInstructions(
        originalInstructions: AdaptiveInstructionGroup,
        effectiveDeltaX: DPixel,
        effectiveDeltaY: DPixel
    ): AdaptiveInstructionGroup {

        val originalPosition = originalInstructions.firstInstanceOf<Position>()
        val newPosition = Position(originalPosition.top + effectiveDeltaY, originalPosition.left + effectiveDeltaX)

        return originalInstructions.removeAll { it is Position } + newPosition

    }

    override fun toString(): String =
        "Move -- mergedDeltaX=$mergedDeltaX  mergedDeltaY=$mergedDeltaY"

}