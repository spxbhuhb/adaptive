package `fun`.adaptive.grove.sheet.model

import `fun`.adaptive.ui.fragment.layout.RawFrame
import kotlin.math.max
import kotlin.math.min

class SheetSelection(
    val items: List<SheetItem>,
    var containingFrame : RawFrame = containingFrame(items)
) {

    fun isEmpty() = items.isEmpty()

    companion object {

        fun containingFrame(items : List<SheetItem>) : RawFrame {
            if (items.isEmpty()) return RawFrame.NaF

            var top = Double.MAX_VALUE
            var left = Double.MAX_VALUE

            var right = Double.MIN_VALUE
            var bottom = Double.MIN_VALUE

            for (item in items) {
                val frame = item.frame
                if (frame == RawFrame.NaF) continue

                val finalTop = frame.top
                val finalLeft = frame.left

                top = min(top, finalTop)
                left = min(left, finalLeft)
                bottom = max(finalTop + frame.height, bottom)
                right = max(finalLeft + frame.width, right)
            }

            return RawFrame(top, left, right - left, bottom - top)
        }

    }
}