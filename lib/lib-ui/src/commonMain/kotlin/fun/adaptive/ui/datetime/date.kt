package `fun`.adaptive.ui.datetime

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.Independent
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.resource.defaultResourceEnvironment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.*
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.support.scroll.scrollIntoView
import `fun`.adaptive.ui.theme.textSmall
import `fun`.adaptive.utility.localDate
import kotlinx.datetime.*


private const val DAY_MODE = 0
private const val MONTH_MODE = 1
private const val YEAR_MODE = 2

@Adaptive
fun datePicker(
    value: LocalDate = localDate(),
    close: () -> Unit,
    onChange: (LocalDate) -> Unit,
    theme: DatetimeTheme = DatetimeTheme.DEFAULT
) {
    @Independent
    val initValue = value

    var mode = DAY_MODE

    grid {
        theme.datePickerContainer

        grid {
            theme.datePickerMonthAndYear
            month(value, { mode = toggleMode(mode, MONTH_MODE) }, onChange)
            year(value, { mode = toggleMode(mode, YEAR_MODE) }, onChange)
        }

        box {
            theme.datePickerInner
            when (mode) {
                DAY_MODE -> dayList(value, theme, onSelected = onChange)
                MONTH_MODE -> monthList(value, theme) { onChange(it); mode = DAY_MODE }
                YEAR_MODE -> yearList(value, theme) { onChange(it); mode = DAY_MODE }
            }
        }

        row {
            theme.datePickerActionsContainer
            text(Strings.cancel) .. theme.datePickerActionText .. onClick { onChange(initValue); close() }
            text(Strings.ok) .. theme.datePickerActionText .. onClick { close() }
        }
    }
}

private fun toggleMode(currentMode: Int, modeToSwitchTo: Int) =
    if (currentMode == modeToSwitchTo) {
        DAY_MODE
    } else {
        modeToSwitchTo
    }

@Adaptive
private fun month(value: LocalDate, switchMode: () -> Unit, onChange: (LocalDate) -> Unit) {
    stepAndSelect(
        monthAbr(value.month),
        { onChange(value.minus(1, DateTimeUnit.MONTH)) },
        { switchMode() },
        { onChange(value.plus(1, DateTimeUnit.MONTH)) }
    )
}

@Adaptive
private fun year(value: LocalDate, switchMode: () -> Unit, onChange: (LocalDate) -> Unit) {
    stepAndSelect(
        value.year.toString(),
        { onChange(value.minus(1, DateTimeUnit.YEAR)) },
        { switchMode() },
        { onChange(value.plus(1, DateTimeUnit.YEAR)) }
    )
}

@Adaptive
private fun stepAndSelect(label: String, stepLeft: () -> Unit, switchMode: () -> Unit, stepRight: () -> Unit) {
    row {
        alignItems.center
        icon(Graphics.chevron_left) .. onClick { stepLeft() }
        box {
            width { 60.dp } .. alignItems.center .. onClick { switchMode() }
            text(label) .. textSmall .. semiBoldFont .. noSelect
            icon(Graphics.arrow_drop_down) .. alignSelf.endCenter .. svgWidth(16.dp) .. svgHeight(16.dp) .. size(16.dp, 16.dp)
        }
        icon(Graphics.chevron_right) .. onClick { stepRight() }
    }
}

@Adaptive
private fun dayList(
    value: LocalDate,
    theme: DatetimeTheme,
    today: LocalDate = localDate(),
    markedDays: List<LocalDate> = emptyList(),
    onSelected: (date: LocalDate) -> Unit
) {
    var startDay = LocalDate(value.year, value.month, 1)

    // find the first day to display, this may be in the previous month
    while (startDay.dayOfWeek != defaultResourceEnvironment.firstDayOfWeek) {
        startDay = startDay.minus(1, DateTimeUnit.DAY)
    }

    val days = (0 .. 41).map { startDay.plus(it, DateTimeUnit.DAY) }

    grid {
        theme.dayListGrid

        for (weekDay in days.subList(0, 7)) {
            box {
                theme.dayBoxBase
                text(dayLetter(weekDay)) .. theme.dayHeader
            }
        }

        for (day in days) {
            day(day, day.month == value.month, day in markedDays, day == today, day == value, theme, onSelected)
        }
    }
}

@Adaptive
private fun day(
    date: LocalDate,
    inMonth: Boolean,
    marked: Boolean,
    today: Boolean,
    selected: Boolean,
    theme: DatetimeTheme = DatetimeTheme.DEFAULT,
    onSelected: (date: LocalDate) -> Unit
) {
    box {
        onClick { onSelected(date) }
        theme.dayBoxInstructions(inMonth, marked, today, selected)
        text(date.dayOfMonth) .. theme.dayInstructions(inMonth, marked, today, selected)
    }
}

private fun dayLetter(date: LocalDate) =
    when (date.dayOfWeek.isoDayNumber) {
        1 -> Strings.mondayOneLetter
        2 -> Strings.tuesdayOneLetter
        3 -> Strings.wednesdayOneLetter
        4 -> Strings.thursdayOneLetter
        5 -> Strings.fridayOneLetter
        6 -> Strings.saturdayOneLetter
        7 -> Strings.sundayOneLetter
        else -> throw IllegalArgumentException("Invalid ISO day number: $date")
    }

@Adaptive
private fun monthList(value: LocalDate, theme: DatetimeTheme, onSelected: (LocalDate) -> Unit) {
    val entries = Month.values() // FIXME use entries, but it causes compilation error

    column {
        maxSize .. verticalScroll
        for (month in entries) {
            listRow(
                monthName(month),
                theme,
                value.month == month
            ) { onSelected(LocalDate(value.year, month, value.dayOfMonth)) }
        }
    }
}

@Adaptive
private fun yearList(value: LocalDate, theme: DatetimeTheme, onSelected: (LocalDate) -> Unit) {
    column {
        maxSize .. verticalScroll
        for (year in 1900 .. 2100) {
            listRow(
                year.toString(),
                theme,
                value.year == year
            ) { onSelected(LocalDate(year, value.month, value.dayOfMonth)) }
        }
    }
}

@Adaptive
private fun listRow(label: String, theme: DatetimeTheme, selected: Boolean, onSelected: () -> Unit): AdaptiveFragment {
    val hover = hover()

    grid {
        theme.listItemContainer(hover, selected)

        onClick { onSelected() }

        if (selected) {
            icon(Graphics.check) .. theme.listItemIcon(hover, selected)
        }

        text(label) .. theme.listItemText(hover, selected)
    }

    if (selected) {
        afterPatchBatch { scrollIntoView(it) }
    }

    return fragment()
}

fun monthAbr(month: Month) = when (month) {
    Month.JANUARY -> Strings.januaryAbr
    Month.FEBRUARY -> Strings.februaryAbr
    Month.MARCH -> Strings.marchAbr
    Month.APRIL -> Strings.aprilAbr
    Month.MAY -> Strings.mayAbr
    Month.JUNE -> Strings.juneAbr
    Month.JULY -> Strings.julyAbr
    Month.AUGUST -> Strings.augustAbr
    Month.SEPTEMBER -> Strings.septemberAbr
    Month.OCTOBER -> Strings.octoberAbr
    Month.NOVEMBER -> Strings.novemberAbr
    Month.DECEMBER -> Strings.decemberAbr
    else -> error("Invalid month: $month")
}

fun monthName(month: Month) = when (month) {
    Month.JANUARY -> Strings.january
    Month.FEBRUARY -> Strings.february
    Month.MARCH -> Strings.march
    Month.APRIL -> Strings.april
    Month.MAY -> Strings.may
    Month.JUNE -> Strings.june
    Month.JULY -> Strings.july
    Month.AUGUST -> Strings.august
    Month.SEPTEMBER -> Strings.september
    Month.OCTOBER -> Strings.october
    Month.NOVEMBER -> Strings.november
    Month.DECEMBER -> Strings.december
    else -> error("Invalid month: $month")
}