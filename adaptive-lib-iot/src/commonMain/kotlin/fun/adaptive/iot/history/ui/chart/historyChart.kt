package `fun`.adaptive.iot.history.ui.chart

import `fun`.adaptive.chart.model.*
import `fun`.adaptive.chart.normalization.DoubleDoubleNormalizer
import `fun`.adaptive.chart.ws.model.WsChartContext
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.canvas.api.canvas
import `fun`.adaptive.graphics.canvas.api.stroke
import `fun`.adaptive.iot.history.model.AioDoubleHistoryRecord
import `fun`.adaptive.iot.history.ui.HistoryContentController
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.utility.format
import kotlin.collections.plusAssign

val xAxis = ChartRenderAxis<Double, Double>(
    size = 49.0,
    offset = 50.0,
    axisLine = true,
    WsChartContext.BASIC_HORIZONTAL_AXIS
) { context, axis, canvasSize ->

    val itemsWidth = canvasSize.width - context.itemOffsetX
    val count = (itemsWidth / 50).toInt()
    val step = 1.0 / count

    val normalizer = context.normalizer

    val out = mutableListOf<ChartRenderMarker>()

    for (i in 1 .. count) {
        val offset = i * step
        out += ChartRenderMarker(
            offset = i * step * itemsWidth,
            tickSize = if (i % 2 == 0) 8.0 else 4.0,
            labelText = normalizer.denormalizeX(offset)?.format(2),
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

    val itemsHeight = canvasSize.height - context.itemOffsetY
    val count = (itemsHeight / 50).toInt() - 1
    val step = 1.0 / count

    val normalizer = context.normalizer

    val out = mutableListOf<ChartRenderMarker>()

    for (i in 1 .. count) {
        val offset = i * step
        out += ChartRenderMarker(
            offset = itemsHeight - offset * itemsHeight,
            tickSize = if (i % 2 == 0) 8.0 else 4.0,
            labelText = normalizer.denormalizeY(offset)?.format(2),
            guide = true
        )
    }

    out
}

val item1 = ChartItem(
    WsChartContext.BASIC_LINE_SERIES,
    listOf(
        ChartDataPoint(0.0, 0.0),
        ChartDataPoint(100.0, 100.0),
        ChartDataPoint(200.0, 50.0),
        ChartDataPoint(300.0, 50.0)
    ),
    instructions = instructionsOf(stroke(0xff00ff))
)

val item2 = ChartItem(
    WsChartContext.BASIC_LINE_SERIES,
    listOf(
        ChartDataPoint(0.0, 10.0),
        ChartDataPoint(100.0, 80.0),
        ChartDataPoint(200.0, 70.0),
        ChartDataPoint(300.0, 60.0)
    ),
    instructions = instructionsOf(stroke(0x00ff00))
)

val items = listOf(item1, item2)

val context = ChartRenderContext<Double, Double>(
    items,
    listOf(xAxis, yAxis),
    50.0,
    50.0,
    { DoubleDoubleNormalizer(it) }
)

@Adaptive
fun historyChart(controller: HistoryContentController, records: List<AioDoubleHistoryRecord>) {
    box {
        maxSize .. padding { 10.dp }

        canvas { canvasSize ->

            for (axis in context.axes) {
                actualize(axis.renderer, emptyInstructions, context, axis, canvasSize)
            }

            for (item in context.items) {
                actualize(item.render, emptyInstructions, context, item, canvasSize)
            }
        }
    }
}