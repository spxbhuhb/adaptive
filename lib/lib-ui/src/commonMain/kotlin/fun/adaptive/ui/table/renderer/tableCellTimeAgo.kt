package `fun`.adaptive.ui.table.renderer

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.generated.resources.*
import `fun`.adaptive.ui.table.TableCellDef
import kotlin.time.Instant

@Adaptive
fun <ITEM> tableCellTimeAgo(cellDef : TableCellDef<ITEM, Instant?>, item : ITEM) : AdaptiveFragment {
    val now = observe { (adapter() as AbstractAuiAdapter<*,*>).tick }.now
    text(cellDef.getFun(item)?.let { timeAgoString(it, now) } ?: "") .. cellDef.instructions(item)
    return fragment()
}

fun timeAgoString(instant : Instant, now : Instant) : String {
    val duration = now - instant

    return when {
        duration.inWholeSeconds < -1L -> Strings.clockDrift
        duration.inWholeSeconds <= 0L -> Strings.justNow
        duration.inWholeSeconds < 60 -> "${duration.inWholeSeconds} ${if (duration.inWholeSeconds == 1L) Strings.secondAgo else Strings.secondsAgo}"
        duration.inWholeMinutes < 60 -> "${duration.inWholeMinutes} ${if (duration.inWholeMinutes == 1L) Strings.minuteAgo else Strings.minutesAgo}"
        duration.inWholeHours < 24 -> "${duration.inWholeHours} ${if (duration.inWholeHours == 1L) Strings.hourAgo else Strings.hoursAgo}"
        duration.inWholeDays < 7 -> "${duration.inWholeDays} ${if (duration.inWholeDays == 1L) Strings.dayAgo else Strings.daysAgo}"
        duration.inWholeDays < 30 -> "${duration.inWholeDays / 7} ${if (duration.inWholeDays / 7 == 1L) Strings.weekAgo else Strings.weeksAgo}"
        duration.inWholeDays < 365 -> "${duration.inWholeDays / 30} ${if (duration.inWholeDays / 30 == 1L) Strings.monthAgo else Strings.monthsAgo}"
        else -> "${duration.inWholeDays / 365} ${if (duration.inWholeDays / 365 == 1L) Strings.yearAgo else Strings.yearsAgo}"
    }
}