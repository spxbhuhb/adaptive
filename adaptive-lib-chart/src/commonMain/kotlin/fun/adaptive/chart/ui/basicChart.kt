package `fun`.adaptive.chart.ui

import `fun`.adaptive.chart.calculation.CalculationContext
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
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.utility.format
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.minutes

val xAxis = ChartAxis<Instant, Double, Unit>(
    size = 49.0,
    offset = 50.0,
    axisLine = true,
    WsChartContext.BASIC_HORIZONTAL_AXIS,
    ::temporalHorizontalAxisMarkers
)

val yAxis = ChartAxis<Instant, Double, Unit>(
    size = 49.0,
    offset = 49.0,
    axisLine = true,
    WsChartContext.BASIC_VERTICAL_AXIS
) { context, axis, canvasSize ->

    val itemsHeight = canvasSize.height - context.itemOffsetY
    val count = (itemsHeight / 50).toInt() - 1
    val step = 1.0 / count

    val normalizer = context.normalizer

    val out = mutableListOf<ChartMarker>()

    for (i in 1 .. count) {
        val offset = i * step
        out += ChartMarker(
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
        ChartDataPoint(Instant.parse("2024-01-01T12:42:00.0Z"), 100.0),
        ChartDataPoint(Instant.parse("2024-01-01T14:00:00.0Z"), 50.0),
        ChartDataPoint(Instant.parse("2024-01-01T15:00:00.0Z"), 50.0)
    ),
    instructions = instructionsOf(stroke(0xff00ff)),
    Unit
)

val item2 = ChartItem(
    WsChartContext.BASIC_LINE_SERIES,
    listOf(
        ChartDataPoint(Instant.parse("2024-01-01T12:00:00.0Z"), 10.0),
        ChartDataPoint(Instant.parse("2024-01-01T13:00:00.0Z"), 80.0),
        ChartDataPoint(Instant.parse("2024-01-01T14:00:00.0Z"), 70.0),
        ChartDataPoint(Instant.parse("2024-01-01T15:00:00.0Z"), 60.0)
    ),
    instructions = instructionsOf(stroke(0x00ff00)),
    Unit
)

val items = listOf(item1, item2)

val context = ChartRenderContext<Instant, Double, Unit>(
    items,
    listOf(xAxis, yAxis),
    50.0,
    50.0,
    { InstantDoubleNormalizer(it) }
)

@Adaptive
fun lineChart(/*chart : Chart*/) {
    var chart = true

    column {
        maxSize .. padding { 10.dp }
        button("toggle") .. onClick { chart = !chart }

        if (chart) {
            canvas { canvasSize ->

                for (axis in context.axes) {
                    actualize(axis.renderer, emptyInstructions, context, axis, canvasSize)
                }

                for (item in context.items) {
                    actualize(item.renderKey, emptyInstructions, context, item, canvasSize)
                }
            }
        } else {
            column {
                multiTable()
            }
        }
    }
}

@Adaptive
fun multiTable() {
    val iStart = now()
    val iEnd = iStart + 15.minutes

    val cc = CalculationContext<Instant,Double, Unit>(
        Instant.parse("2024-01-01T12:00:00.0Z"),
        Instant.parse("2024-01-01T15:00:00.0Z"),
        context.normalizer.normalizedInterval(iStart, iEnd),
        context.normalizer
    ) { chartItem, start, end ->
        chartItem.sourceData[start].y
    }

    val markerColumn = cc.markers
    val valueColumns = context.items.map { it.normalize(cc.normalizer).prepareCells(cc) }

    for (i in 0 until markerColumn.size) {
        row {
            gap { 16.dp }
            text(markerColumn[i])
            for (valueColumn in valueColumns) {
                text(valueColumn.cells[i])
            }
        }
    }
}
