package `fun`.adaptive.chart.ui

import `fun`.adaptive.chart.model.ChartRenderAxis
import `fun`.adaptive.chart.model.ChartRenderMarker
import `fun`.adaptive.chart.model.ChartRenderPoint
import `fun`.adaptive.chart.model.ChartRenderSeries
import `fun`.adaptive.chart.ws.model.WsChartContext
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.graphics.canvas.api.canvas
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.color
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.instruction.dp


val xAxis = ChartRenderAxis<Double, Double>(
    size = 49.0,
    offset = 50.0,
    axisLine = true,
    WsChartContext.BASIC_HORIZONTAL_AXIS
) { context, axis, canvasSize ->

    val count = (canvasSize.width / 50).toInt()
    val out = mutableListOf<ChartRenderMarker>()

    for (i in 1 .. count) {
        out += ChartRenderMarker(
            offset = i * 50.0,
            tickSize = if (i % 2 == 0) 8.0 else 4.0,
            labelText = "${i * 50}",
            guide = true
        )
    }

    out
}

val yAxis = ChartRenderAxis<Double, Double>(
    size = 49.0,
    offset = 49.0,
    axisLine = true,
    WsChartContext.BASIC_VERTICAL_AXIS
) { context, axis, canvasSize ->

    val count = (canvasSize.height / 50).toInt() - 1
    val out = mutableListOf<ChartRenderMarker>()

    for (i in 1 .. count) {
        out += ChartRenderMarker(
            offset = canvasSize.height - i * 50.0 - 50.0,
            tickSize = if (i % 2 == 0) 8.0 else 4.0,
            labelText = "${i * 50}",
            guide = true
        )
    }

    out
}

val series1 = ChartRenderSeries(
    offsetX = 50.0,
    offsetY = 50.0,
    color(0xff00ff),
    listOf(
        ChartRenderPoint(0.0, 0.0),
        ChartRenderPoint(100.0, 100.0),
        ChartRenderPoint(200.0, 50.0),
        ChartRenderPoint(300.0, 50.0)
    ),
    WsChartContext.BASIC_LINE_SERIES
)

val series2 = ChartRenderSeries(
    offsetX = 50.0,
    offsetY = 50.0,
    color(0x00ff00),
    listOf(
        ChartRenderPoint(0.0, 10.0),
        ChartRenderPoint(100.0, 80.0),
        ChartRenderPoint(200.0, 70.0),
        ChartRenderPoint(300.0, 60.0)
    ),
    WsChartContext.BASIC_LINE_SERIES
)

val context = ChartRenderContext<Double, Double>(
    listOf(xAxis, yAxis),
    listOf(series1, series2),
    0.0, 400.0, 0.0, 400.0,
    ChartTheme()
)

@Adaptive
fun lineChart(/*chart : Chart*/) {
    box {
        maxSize .. padding { 10.dp }

        canvas { canvasSize ->

            for (axis in context.axes) {
                actualize(axis.renderer, emptyInstructions, context, axis, canvasSize)
            }

            for (series in context.series) {
                actualize(series.renderer, emptyInstructions, context, series, canvasSize)
            }
        }
    }
}