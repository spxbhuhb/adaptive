package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.grove.sheet.SheetViewBackend
import `fun`.adaptive.ui.fragment.layout.RawFrame
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.layout.Position

@Adat
class Move(
    override val start: Long,
    override var transformX: DPixel,
    override var transformY: DPixel
) : Transform() {

    override val withSizes: Boolean
        get() = false

    override fun newFrame(controller: SheetViewBackend): RawFrame =
        startFrame.move(controller.toPx(transformX), controller.toPx(transformY))

    override fun newInstructions(cacheIndex: Int): AdaptiveInstruction {

        val originalPosition = positionCache[cacheIndex]
        val newPosition = Position(originalPosition.top + transformY, originalPosition.left + transformX)

        return instructionCache[cacheIndex] + newPosition
    }

    override fun toString(): String =
        "Move -- transformX=$transformX  transformY=$transformY"

}