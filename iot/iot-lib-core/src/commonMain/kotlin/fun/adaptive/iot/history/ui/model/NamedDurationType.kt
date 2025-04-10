package `fun`.adaptive.iot.history.ui.model

import `fun`.adaptive.iot.generated.resources.*
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.utility.*
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlin.time.Duration.Companion.days

enum class NamedDurationType(
    val labelFun: () -> String,
    val calcFun: () -> Pair<LocalDate, LocalDate>
) {
    Today({ Strings.today }, {
        val today = localDate()
        today to today
    }),

    Yesterday({ Strings.yesterday }, {
        val yesterday = now().minus(1.days).localDate()
        yesterday to yesterday
    }),

    ThisWeek({ Strings.thisWeek }, {
        val today = localDate()
        val startOfWeek = now().minus(today.dayOfWeek.isoDayNumber.days)
        val endOfWeek = startOfWeek.plus(6.days)
        startOfWeek.localDate() to endOfWeek.localDate()
    }),

    LastWeek({ Strings.lastWeek }, {
        val today = localDate()
        val startOfThisWeek = now().minus(today.dayOfWeek.isoDayNumber.days)
        val startOfLastWeek = startOfThisWeek.minus(7.days)
        val endOfLastWeek = startOfLastWeek.plus(6.days)
        startOfLastWeek.localDate() to endOfLastWeek.localDate()
    }),

    LastMonth({ Strings.lastMonth }, {
        val lastMonth = localDate().previousMonth
        val start = lastMonth.firstDayOfMonth
        val end = lastMonth.lastDayOfMonth
        start to end
    }),

    TwoMonthsBefore({Strings.twoMonthsBefore}, {
        val lastMonth = localDate()
        val start = lastMonth.twoMonthsBefore
        val end = lastMonth.twoMonthsBefore
        start to end
    }),

    Custom({ Strings.custom }, {
        // Placeholder – should be set externally when using Custom
        val today = localDate()
        today to today
    })
}