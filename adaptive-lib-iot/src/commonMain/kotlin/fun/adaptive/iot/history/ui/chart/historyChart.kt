package `fun`.adaptive.iot.history.ui.chart

import `fun`.adaptive.chart.model.ChartItem
import `fun`.adaptive.chart.model.ChartAxis
import `fun`.adaptive.chart.model.ChartRenderContext
import `fun`.adaptive.chart.normalization.InstantDoubleNormalizer
import `fun`.adaptive.chart.ui.temporal.doubleVerticalAxisMarkers
import `fun`.adaptive.chart.ui.temporal.temporalHorizontalAxisMarkers
import `fun`.adaptive.chart.ws.model.WsChartContext
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.graphics.canvas.api.canvas
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.instruction.dp
import kotlinx.datetime.Instant

@Adaptive
fun historyChart(chartItems: List<ChartItem<Instant,Double>>) {

    val xAxis = ChartAxis<Instant, Double>(
        size = 49.0,
        offset = 50.0,
        axisLine = true,
        WsChartContext.BASIC_HORIZONTAL_AXIS,
        ::temporalHorizontalAxisMarkers
    )

    val yAxis = ChartAxis<Instant, Double>(
        size = 49.0,
        offset = 49.0,
        axisLine = true,
        WsChartContext.BASIC_VERTICAL_AXIS,
        ::doubleVerticalAxisMarkers
    )

    val context = ChartRenderContext<Instant, Double>(
        chartItems,
        listOf(xAxis, yAxis),
        50.0,
        50.0,
        { InstantDoubleNormalizer(it) }
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

//     val chartItems =
//        ChartItem(
//            WsChartContext.BASIC_LINE_SERIES,
//            records.map { record -> ChartDataPoint(record.timestamp, record.value) },
//            instructions = instructionsOf(stroke(0xff00ff))
//        )