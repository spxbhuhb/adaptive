package `fun`.adaptive.chart.ui.temporal

import `fun`.adaptive.chart.model.ChartAxis
import `fun`.adaptive.chart.model.ChartMarker
import `fun`.adaptive.chart.model.ChartRenderContext
import `fun`.adaptive.ui.fragment.layout.RawSize
import `fun`.adaptive.utility.p02
import kotlin.time.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

fun temporalHorizontalAxisMarkers(
    context: ChartRenderContext<Instant, *, *>,
    @Suppress("unused") axis: ChartAxis<Instant, *, *>,
    canvasSize: RawSize
): List<ChartMarker> {
    val range = context.range ?: return emptyList()

    val availableHeight = canvasSize.width - context.plotPadding.start - context.plotPadding.end
    val approxLabelSpacing = 80.0

    val markerRange: Duration

    calculateMarkerPositions(
        availableHeight,
        approxLabelSpacing,
        range.xStart,
        range.xEnd
    ).also {

        markerRange = if (it.size >= 2) it[1].second - it[0].second else Duration.ZERO

    }.map { (offset, value) ->

        ChartMarker(
            offset = offset,
            labelText = instantLabelText(value, markerRange),
            guide = true
        )
    }.also {
        return it
    }

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


fun calculateMarkerPositions(
    availableSize: Double,
    stepSize: Double,
    start: Instant,
    end: Instant
): List<Pair<Double, Instant>> {
    val totalDuration = end - start
    val maxCount = availableSize / stepSize
    val stepDuration = niceDuration(totalDuration / maxCount, true)
    val totalMillis = totalDuration.inWholeMilliseconds.toDouble()
    val millisPerPixel = totalMillis / availableSize
    val stepOffset = stepDuration.inWholeMilliseconds / millisPerPixel

    val result = mutableListOf<Pair<Double, Instant>>()
    var offset = 0.0
    var current = start

    while (offset < availableSize) {
        result += offset to current
        current += stepDuration
        offset += stepOffset
    }

    return result
}

fun niceDuration(range: Duration, round: Boolean): Duration {
    return if (round) {
        durationCandidates.firstOrNull { it >= range } ?: durationCandidates.last()
    } else {
        durationCandidates.firstOrNull { it > range } ?: durationCandidates.last()
    }
}

val durationCandidates = listOf(
    1.milliseconds,
    2.milliseconds,
    5.milliseconds,
    10.milliseconds,
    20.milliseconds,
    50.milliseconds,
    100.milliseconds,
    200.milliseconds,
    500.milliseconds,
    1.seconds,
    2.seconds,
    5.seconds,
    10.seconds,
    15.seconds,
    30.seconds,
    1.minutes,
    2.minutes,
    5.minutes,
    10.minutes,
    15.minutes,
    30.minutes,
    1.hours,
    2.hours,
    3.hours,
    6.hours,
    12.hours,
    1.days,
    2.days,
    7.days
)