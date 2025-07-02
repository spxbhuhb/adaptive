package `fun`.adaptive.ui.input.datetime

import `fun`.adaptive.ui.fragment.structural.PopupSourceViewBackend
import `fun`.adaptive.ui.input.InputViewBackend
import `fun`.adaptive.ui.input.date.DateInputTheme
import kotlinx.datetime.LocalDateTime

class DateTimeInputViewBackend(
    value: LocalDateTime? = null,
    label: String? = null,
    isSecret: Boolean = false
) : InputViewBackend<LocalDateTime, DateTimeInputViewBackend>(
    value, label, isSecret
), PopupSourceViewBackend {

    val dateInputTheme = DateInputTheme.default

}