package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.grove.sheet.model.SheetItem
import `fun`.adaptive.ui.instruction.layout.Frame
import kotlin.math.max
import kotlin.math.min

@Adat
class SelectByFrame(
    val frame : Frame,
    override val additional: Boolean
) : Select() {

    override fun SheetViewController.findItems() : List<SheetItem> {
        val px1 = frame.left.px
        val py1 = frame.top.px
        val px2 = px1 + frame.width.px
        val py2 = py1 + frame.height.px

        val x1 = min(px1, px2)
        val y1 = min(py1, py2)
        val x2 = max(px1, px2)
        val y2 = max(py1, py2)

        return findByRenderData { renderData ->
            val rx1 = renderData.finalLeft
            val ry1 = renderData.finalTop
            val rx2 = rx1 + renderData.finalWidth
            val ry2 = ry1 + renderData.finalHeight

            when {
                x1 >= rx2 || rx1 >= x2 -> false          // one rectangle is to the left of the other
                y1 >= ry2 || ry1 >= y2 -> false          // one rectangle is above the other
                else -> true
            }
        }
    }

    override fun toString(): String =
        "Select by frame $frame $traceString"

}