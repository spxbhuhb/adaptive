package `fun`.adaptive.ui.input.datetime

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.date.dateInputBackend
import `fun`.adaptive.ui.input.date.dateInputInner
import `fun`.adaptive.ui.input.date.datePicker
import `fun`.adaptive.ui.input.decoratedInput
import `fun`.adaptive.ui.input.time.timeInput
import `fun`.adaptive.ui.input.time.timeInputBackend
import `fun`.adaptive.ui.input.time.timeInputInner
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.utility.localDate
import `fun`.adaptive.utility.localTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

@Adaptive
fun dateTimeInput(
    viewBackend: DateTimeInputViewBackend
): AdaptiveFragment {

    val observed = observe { viewBackend }
    val dateBackend = observe { dateInputBackend(viewBackend.inputValue?.date ?: localDate()) }
    val timeBackend = observe { timeInputBackend(viewBackend.inputValue?.time ?: localTime()) }

    val focus = focus()

    decoratedInput(focus, observed) {
        row(instructions()) {
            gap { 8.dp }

            dateInputInner(dateBackend)
            timeInputInner(timeBackend)
        }
    }

    return fragment()
}