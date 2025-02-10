package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.grove.sheet.ControlNames
import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.ui.fragment.layout.RawFrame
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.instruction.layout.Size

class Resize(
    start: Long,
    deltaX: DPixel,
    deltaY: DPixel,
    activeControl: String?
) : Transform(start, deltaX, deltaY) {

    val topLeft = activeControl in ControlNames.topLeftControls

    override fun newFrame(
        controller: SheetViewController,
        effectiveDeltaX: DPixel,
        effectiveDeltaY: DPixel
    ): RawFrame {
        TODO("Not yet implemented")
    }

    override fun newInstructions(
        originalInstructions: AdaptiveInstructionGroup,
        effectiveDeltaX: DPixel,
        effectiveDeltaY: DPixel
    ) : AdaptiveInstructionGroup {
        val originalSize = originalInstructions.firstInstanceOf<Size>()
        val newSize = Size(originalSize.width + effectiveDeltaX, originalSize.height + effectiveDeltaY)

        if (topLeft) {
            val originalPosition = originalInstructions.firstInstanceOf<Position>()
            val newPosition = Position(originalPosition.top + effectiveDeltaY, originalPosition.left + effectiveDeltaX)
            return originalInstructions.removeAll { it is Size || it is Position } + newSize + newPosition
        } else {
            return originalInstructions.removeAll { it is Size } + newSize
        }
    }


    override fun toString(): String = "Resize -- topLeft: $topLeft  mergedDeltaX=$mergedDeltaX  mergedDeltaY=$mergedDeltaY"

}