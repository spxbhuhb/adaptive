package `fun`.adaptive.chart.ui

import `fun`.adaptive.chart.model.*
import `fun`.adaptive.chart.normalization.InstantDoubleNormalizer
import `fun`.adaptive.chart.ui.temporal.temporalHorizontalAxisMarkers
import `fun`.adaptive.chart.ws.model.WsChartContext
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.canvas.api.canvas
import `fun`.adaptive.graphics.canvas.api.stroke
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.utility.format
import kotlinx.datetime.Instant

val xAxis = ChartRenderAxis<Instant, Double>(
    size = 49.0,
    offset = 50.0,
    axisLine = true,
    WsChartContext.BASIC_HORIZONTAL_AXIS,
    ::temporalHorizontalAxisMarkers
)

val yAxis = ChartRenderAxis<Instant, Double>(
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
            labelText = normalizer.denormalizeY(offset)?.format(1),
            labelOffset = 0.0,
            guide = true
        )
    }

    out
}

val item1 = ChartItem(
    WsChartContext.BASIC_LINE_SERIES,
    listOf(
        ChartDataPoint(Instant.parse("2024-01-01T12:00:00.0Z"), 0.0),
        ChartDataPoint(Instant.parse("2024-01-01T13:00:00.0Z"), 100.0),
        ChartDataPoint(Instant.parse("2024-01-01T14:00:00.0Z"), 50.0),
        ChartDataPoint(Instant.parse("2024-01-01T15:00:00.0Z"), 50.0)
    ),
    instructions = instructionsOf(stroke(0xff00ff))
)

val item2 = ChartItem(
    WsChartContext.BASIC_LINE_SERIES,
    listOf(
        ChartDataPoint(Instant.parse("2024-01-01T12:00:00.0Z"), 10.0),
        ChartDataPoint(Instant.parse("2024-01-01T13:00:00.0Z"), 80.0),
        ChartDataPoint(Instant.parse("2024-01-01T14:00:00.0Z"), 70.0),
        ChartDataPoint(Instant.parse("2024-01-01T15:00:00.0Z"), 60.0)
    ),
    instructions = instructionsOf(stroke(0x00ff00))
)

val items = listOf(item1, item2)

val context = ChartRenderContext<Instant, Double>(
    items,
    listOf(xAxis, yAxis),
    50.0,
    50.0,
    { InstantDoubleNormalizer(it) }
)

@Adaptive
fun lineChart(/*chart : Chart*/) {
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