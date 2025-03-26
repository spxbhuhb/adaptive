package `fun`.adaptive.chart.model

import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.ui.fragment.layout.RawSize

/**
 * @property size     Height for X, width for Y. Size of the axis in the direction perpendicular
 *                    to the direction of the axis line.
 * @property offset   Y offset for X, X offset for Y from the top-left corner of the canvas. X is typically
 *                    moved down to the bottom, Y is typically almost at the beginning but decorations may
 *                    change that.
 * @property axisLine Show the actual line or not.
 */
class ChartAxis<XT : Comparable<XT>, YT : Comparable<YT>>(
    val size: Double,
    val offset: Double,
    val axisLine: Boolean,
    val renderer: FragmentKey,
    val markerFun: (ChartRenderContext<XT, YT>, ChartAxis<XT, YT>, RawSize) -> List<ChartMarker>
) {

    fun markers(context: ChartRenderContext<*, *>, canvasSize: RawSize): List<ChartMarker> {
        @Suppress("UNCHECKED_CAST") // couldn't find a way to use function references for axis builder and keep type safety
        return markerFun(context as ChartRenderContext<XT, YT>, this, canvasSize)
    }

}