package `fun`.adaptive.chart.ui.temporal

import `fun`.adaptive.chart.model.ChartRenderAxis
import `fun`.adaptive.chart.model.ChartRenderContext
import `fun`.adaptive.chart.model.ChartRenderMarker
import `fun`.adaptive.ui.fragment.layout.RawSize
import `fun`.adaptive.utility.p02
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

fun temporalHorizontalAxisMarkers(
    context : ChartRenderContext<Instant, *>,
    @Suppress("unused") axis : ChartRenderAxis<Instant, *>,
    canvasSize : RawSize
) : List<ChartRenderMarker> {
    val range = context.range ?: return emptyList()

    val itemsWidth = canvasSize.width - context.itemOffsetX
    val count = (itemsWidth / 50).toInt()
    val step = 1.0 / count

    val normalizer = context.normalizer

    val xRange = range.xEnd - range.xStart
    val tickRange = xRange / count

    val out = mutableListOf<ChartRenderMarker>()

    for (i in 1 .. count - 1) {
        val offset = i * step
        out += ChartRenderMarker(
            offset = i * step * itemsWidth,
            tickSize = if (i % 2 == 0) 8.0 else 4.0,
            labelText = instantLabelText(normalizer.denormalizeX(offset), tickRange),
            labelOffset = 0.0,
            guide = true
        )
    }

    return out
}

fun instantLabelText(value: Instant?, tickRange: Duration) : String {
    if (value == null) return ""

    val localDateTime = value.toLocalDateTime(TimeZone.currentSystemDefault())

    return when {
        tickRange < 1.seconds -> value.toString()
        tickRange < 1.minutes-> "${localDateTime.minute.p02}:${localDateTime.second.p02}"
        tickRange < 1.hours -> "${localDateTime.hour}:${localDateTime.minute.p02}" // Show hour and minute
        tickRange < 1.days -> "${localDateTime.month}-${localDateTime.dayOfMonth} ${localDateTime.hour.p02}" // Include date and hour
        else -> "${localDateTime.month} ${localDateTime.dayOfMonth}" // Display only the date
    }
}
