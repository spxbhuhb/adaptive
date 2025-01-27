package `fun`.adaptive.grove.sheet

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.ui.api.frame
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Frame
import kotlin.math.max
import kotlin.math.min

@Adat
class SheetSelection(
    val selected: List<SelectionInfo>
) {
    fun containingFrame(): Frame? {
        if (selected.isEmpty()) return null

        var top = Double.MAX_VALUE
        var left = Double.MAX_VALUE

        var right = Double.MIN_VALUE
        var bottom = Double.MIN_VALUE

        for (item in selected) {
            val frame = item.frame

            val finalTop = frame.top
            val finalLeft = frame.left

            top = min(top, finalTop)
            left = min(left, finalLeft)
            bottom = max(finalTop + frame.height, bottom)
            right = max(finalLeft + frame.width, left)
        }

        return frame(top.dp, left.dp, (right - left).dp, (bottom - top).dp)
    }
}