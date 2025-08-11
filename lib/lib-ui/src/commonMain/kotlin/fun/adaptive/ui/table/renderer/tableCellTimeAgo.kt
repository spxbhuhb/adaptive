package `fun`.adaptive.ui.table.renderer

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.producer.poll
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.generated.resources.dayAgo
import `fun`.adaptive.ui.generated.resources.justNow
import `fun`.adaptive.ui.generated.resources.minutesAgo
import `fun`.adaptive.ui.generated.resources.secondAgo
import `fun`.adaptive.ui.generated.resources.secondsAgo
import `fun`.adaptive.ui.generated.resources.hoursAgo
import `fun`.adaptive.ui.generated.resources.daysAgo
import `fun`.adaptive.ui.generated.resources.hourAgo
import `fun`.adaptive.ui.generated.resources.minuteAgo
import `fun`.adaptive.ui.generated.resources.monthAgo
import `fun`.adaptive.ui.generated.resources.weeksAgo
import `fun`.adaptive.ui.generated.resources.monthsAgo
import `fun`.adaptive.ui.generated.resources.weekAgo
import `fun`.adaptive.ui.generated.resources.yearAgo
import `fun`.adaptive.ui.generated.resources.yearsAgo
import `fun`.adaptive.ui.table.TableCellDef
import kotlin.time.Clock.System.now
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

@Adaptive
fun <ITEM> tableCellTimeAgo(cellDef : TableCellDef<ITEM, Instant?>, item : ITEM) : AdaptiveFragment {
    val now = poll(1.seconds) { now() } ?: now()
    text(cellDef.getFun(item)?.let { timeAgoString(it, now) } ?: "") .. cellDef.instructions
    return fragment()
}

fun timeAgoString(instant : Instant, now : Instant) : String {
    val duration = now - instant

    return when {
        duration.inWholeSeconds == 0L -> Strings.justNow
        duration.inWholeSeconds < 60 -> "${duration.inWholeSeconds} ${if (duration.inWholeSeconds == 1L) Strings.secondAgo else Strings.secondsAgo}"
        duration.inWholeMinutes < 60 -> "${duration.inWholeMinutes} ${if (duration.inWholeMinutes == 1L) Strings.minuteAgo else Strings.minutesAgo}"
        duration.inWholeHours < 24 -> "${duration.inWholeHours} ${if (duration.inWholeHours == 1L) Strings.hourAgo else Strings.hoursAgo}"
        duration.inWholeDays < 7 -> "${duration.inWholeDays} ${if (duration.inWholeDays == 1L) Strings.dayAgo else Strings.daysAgo}"
        duration.inWholeDays < 30 -> "${duration.inWholeDays / 7} ${if (duration.inWholeDays / 7 == 1L) Strings.weekAgo else Strings.weeksAgo}"
        duration.inWholeDays < 365 -> "${duration.inWholeDays / 30} ${if (duration.inWholeDays / 30 == 1L) Strings.monthAgo else Strings.monthsAgo}"
        else -> "${duration.inWholeDays / 365} ${if (duration.inWholeDays / 365 == 1L) Strings.yearAgo else Strings.yearsAgo}"
    }
}