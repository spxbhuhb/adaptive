package `fun`.adaptive.iot.history.ui.chart

import `fun`.adaptive.chart.model.ChartAxis
import `fun`.adaptive.chart.model.ChartRenderContext
import `fun`.adaptive.chart.ui.temporal.doubleVerticalAxisMarkers
import `fun`.adaptive.chart.ui.temporal.temporalHorizontalAxisMarkers
import `fun`.adaptive.chart.ws.model.WsChartContext
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.graphics.canvas.api.canvas
import `fun`.adaptive.iot.history.model.AioDoubleHistoryRecord
import `fun`.adaptive.iot.history.ui.HistoryContentController
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.value.item.AvItem
import kotlinx.datetime.Instant

@Adaptive
fun historyChart(controller: HistoryContentController) {

    val chartItems = valueFrom { controller.chartItems }

    val xAxis = ChartAxis<Instant, AioDoubleHistoryRecord, AvItem<*>>(
        size = 49.0,
        offset = 50.0,
        axisLine = true,
        WsChartContext.BASIC_HORIZONTAL_AXIS,
        ::temporalHorizontalAxisMarkers
    )

    val yAxis = ChartAxis<Instant, AioDoubleHistoryRecord, AvItem<*>>(
        size = 49.0,
        offset = 49.0,
        axisLine = true,
        WsChartContext.BASIC_VERTICAL_AXIS,
        { c, a, s -> doubleVerticalAxisMarkers(c, a, s) { it.value } }
    )

    val context = ChartRenderContext<Instant, AioDoubleHistoryRecord, AvItem<*>>(
        chartItems,
        listOf(xAxis, yAxis),
        50.0,
        50.0,
        { DoubleHistoryValueNormalizer(it) }
    )

    box {
        maxSize .. padding { 10.dp }

        canvas { canvasSize ->

            for (axis in context.axes) {
                actualize(axis.renderer, emptyInstructions, context, axis, canvasSize)
            }

            for (item in context.items) {
                actualize(item.renderKey, emptyInstructions, context, item, canvasSize)
            }
        }
    }
}