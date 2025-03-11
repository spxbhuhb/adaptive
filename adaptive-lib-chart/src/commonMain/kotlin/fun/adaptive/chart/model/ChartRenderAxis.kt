package `fun`.adaptive.chart.model

import `fun`.adaptive.chart.ui.ChartRenderContext
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.ui.fragment.layout.RawSize

class ChartRenderAxis<XT,YT>(
    val size : Double,
    val offset: Double,
    val axisLine: Boolean,
    val renderer: FragmentKey,
    val markerFun : (ChartRenderContext<XT,YT>, ChartRenderAxis<XT,YT>, RawSize) -> List<ChartRenderMarker>
) {

    fun markers(context : ChartRenderContext<*,*>, canvasSize : RawSize) : List<ChartRenderMarker> {
        @Suppress("UNCHECKED_CAST") // couldn't find a way to use function references for axis builder and keep type safety
        return markerFun(context as ChartRenderContext<XT,YT>, this, canvasSize)
    }

}