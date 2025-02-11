package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.ui.fragment.layout.RawFrame
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.layout.Position

open class Move(
    start: Long,
    deltaX: DPixel,
    deltaY: DPixel
) : Transform(start, deltaX, deltaY, withSizes = false) {

    override fun newFrame(controller: SheetViewController): RawFrame =
        startFrame.move(controller.toPx(transformX), controller.toPx(transformY))

    override fun newInstructions(cacheIndex: Int): AdaptiveInstructionGroup {

        val originalPosition = positionCache[cacheIndex]
        val newPosition = Position(originalPosition.top + transformY, originalPosition.left + transformX)

        return instructionCache[cacheIndex] + newPosition

    }

    override fun toString(): String =
        "Move -- transformX=$transformX  transformY=$transformY"

}