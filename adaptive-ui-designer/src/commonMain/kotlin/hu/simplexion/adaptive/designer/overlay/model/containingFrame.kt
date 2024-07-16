package hu.simplexion.adaptive.designer.overlay.model

import hu.simplexion.adaptive.designer.utility.Selection
import hu.simplexion.adaptive.ui.common.instruction.Frame
import hu.simplexion.adaptive.ui.common.instruction.dp
import hu.simplexion.adaptive.ui.common.instruction.frame
import kotlin.math.max
import kotlin.math.min

fun containingFrame(selection: Selection): Frame? {
    selection.forEach { println(it) }
    if (selection.isEmpty()) return null

    var top = Double.MAX_VALUE
    var left = Double.MAX_VALUE

    var right = Double.MIN_VALUE
    var bottom = Double.MIN_VALUE

    for (fragment in selection) {
        val renderData = fragment.renderData
        val finalTop = renderData.finalTop
        val finalLeft = renderData.finalLeft

        top = min(top, finalTop)
        left = min(left, finalLeft)
        bottom = max(finalTop + renderData.finalHeight, bottom)
        right = max(finalLeft + renderData.finalWidth, left)
    }

    return frame(top.dp, left.dp, (right - left).dp, (bottom - top).dp).also { println(it) }
}