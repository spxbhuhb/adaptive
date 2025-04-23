package `fun`.adaptive.sandbox.recipe.chart

import `fun`.adaptive.chart.app.ChartModule
import `fun`.adaptive.chart.calculation.CalculationContext
import `fun`.adaptive.chart.model.*
import `fun`.adaptive.chart.normalization.InstantDoubleNormalizer
import `fun`.adaptive.chart.ui.temporal.doubleVerticalAxisMarkers
import `fun`.adaptive.chart.ui.temporal.temporalHorizontalAxisMarkers
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.canvas.api.canvas
import `fun`.adaptive.graphics.canvas.api.stroke
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.fragment.layout.RawSurrounding
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.utility.format
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.minutes

val xAxis = ChartAxis<Instant, Double, Unit>(
    size = 49.0,
    offset = 50.0,
    axisLine = true,
    ChartModule.BASIC_HORIZONTAL_AXIS,
    ::temporalHorizontalAxisMarkers
)

val yAxis = ChartAxis<Instant, Double, Unit>(
    size = 49.0,
    offset = 49.0,
    axisLine = true,
    ChartModule.BASIC_VERTICAL_AXIS,
    { c, a, s -> doubleVerticalAxisMarkers(c, a, s) { it } }
)

val item1 = ChartItem(
    ChartModule.BASIC_LINE_SERIES,
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
    ChartModule.BASIC_LINE_SERIES,
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
    RawSurrounding(0.0, 0.0, 50.0, 50.0),
    0.0,
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
