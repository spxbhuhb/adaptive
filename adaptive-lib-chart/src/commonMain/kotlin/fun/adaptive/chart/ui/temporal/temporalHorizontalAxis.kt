package `fun`.adaptive.chart.ui.temporal

import `fun`.adaptive.chart.model.ChartAxis
import `fun`.adaptive.chart.model.ChartRenderContext
import `fun`.adaptive.chart.model.ChartMarker
import `fun`.adaptive.ui.fragment.layout.RawSize
import `fun`.adaptive.utility.p02
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

fun temporalHorizontalAxisMarkers(
    context: ChartRenderContext<Instant, *, *>,
    @Suppress("unused") axis: ChartAxis<Instant, *, *>,
    canvasSize: RawSize
): List<ChartMarker> {
    val range = context.range ?: return emptyList()

    val itemsWidth = canvasSize.width - context.itemOffsetX
    val count = (itemsWidth / 100).toInt()
    val step = 1.0 / count

    val normalizer = context.normalizer

    val xRange = range.xEnd - range.xStart
    val tickRange = xRange / count

    val out = mutableListOf<ChartMarker>()

    for (i in 1 .. count - 1) {
        val offset = i * step
        out += ChartMarker(
            offset = i * step * itemsWidth,
            tickSize = if (i % 2 == 0) 8.0 else 4.0,
            labelText = instantLabelText(normalizer.denormalizeX(offset), tickRange),
            labelOffset = 0.0,
            guide = true
        )
    }

    return out
}

fun instantLabelText(value: Instant?, tickRange: Duration): String {
    if (value == null) return ""

    val localDateTime = value.toLocalDateTime(TimeZone.currentSystemDefault())

    return when {
        tickRange < 1.seconds -> localDateTime.dayAndTime()
        tickRange < 1.minutes -> "${localDateTime.minute.p02}:${localDateTime.second.p02}"
        tickRange < 1.hours -> "${localDateTime.hour.p02}:${localDateTime.minute.p02}" // Show hour and minute
        tickRange < 1.days -> localDateTime.dayAndTime() // Include date and hour
        else -> "${(localDateTime.month.ordinal + 1).p02}.${localDateTime.dayOfMonth.p02}." // Display only the date
    }
}

fun LocalDateTime.dayAndTime() =
    "${(month.ordinal + 1).p02}.${dayOfMonth.p02}. ${hour.p02}:${minute.p02}"