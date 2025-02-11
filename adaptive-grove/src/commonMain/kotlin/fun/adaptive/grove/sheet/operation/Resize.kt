package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.grove.sheet.model.HandleInfo
import `fun`.adaptive.ui.fragment.layout.RawFrame
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Frame
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.instruction.layout.Size

class Resize(
    start: Long,
    deltaX: DPixel,
    deltaY: DPixel,
    val handleInfo: HandleInfo
) : Transform(start, deltaX, deltaY, withSizes = true) {

    var originalFrame = Frame.NaF

    var widthRatio = Double.NaN
    var heightRatio = Double.NaN

    override fun initialize(controller: SheetViewController) {
        super.initialize(controller)
        originalFrame = controller.toFrame(startFrame)
        calculateRatio()
    }

    override fun merge(last: Transform) {
        super.merge(last)

        if (last !is Resize) return

        originalFrame = last.originalFrame
        calculateRatio()
    }

    fun calculateRatio() {
        var originalFrameWidth = originalFrame.width.value
        var originalFrameHeight = originalFrame.height.value

        var newFrameWidth = originalFrameWidth + if (handleInfo.xReverse) - transformX.value else transformX.value
        var newFrameHeight = originalFrameHeight + if (handleInfo.yReverse) - transformY.value else transformY.value

        widthRatio = originalFrameWidth / newFrameWidth
        heightRatio = originalFrameHeight / newFrameHeight
    }

    override fun newFrame(
        controller: SheetViewController
    ): RawFrame {

        val (newSize, newPosition) = resizeFrame(
            originalFrame.position,
            originalFrame.size
        )

        return controller.toRawFrame(newPosition.top, newPosition.left, newSize.width, newSize.height)
    }

    override fun newInstructions(
        cacheIndex: Int,
    ): AdaptiveInstructionGroup {

        val (newSize, newPosition) = resizeFrame(
            positionCache[cacheIndex],
            sizeCache[cacheIndex],
        )

        return instructionCache[cacheIndex] + newSize + newPosition
    }

    override fun toString(): String = "Resize -- handle: ${handleInfo.name}  transformX=$transformX  transformY=$transformY"

    fun resizeFrame(
        originalPosition: Position,
        originalSize: Size
    ): Pair<Size, Position> {

        var newLeft = originalPosition.left.value
        var newWidth = originalSize.width.value

        if (handleInfo.xActive) {
            newWidth = newWidth / widthRatio
        }

        if (newWidth < 0.0) {
            newLeft += newWidth
            newWidth = - newWidth
        }

        var newTop = originalPosition.top.value
        var newHeight = originalSize.height.value

        if (handleInfo.yActive) {
            newHeight = newHeight / heightRatio
        }

        if (newHeight < 0.0) {
            newTop += newHeight
            newHeight = - newHeight
        }

        return Size(newWidth.dp, newHeight.dp) to Position(newTop.dp, newLeft.dp)
    }
}