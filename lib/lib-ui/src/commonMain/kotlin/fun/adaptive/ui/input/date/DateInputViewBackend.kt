package `fun`.adaptive.ui.input.date

import `fun`.adaptive.ui.fragment.structural.PopupSourceViewBackend
import `fun`.adaptive.ui.input.InputViewBackend
import kotlinx.datetime.LocalDate

class DateInputViewBackend(
    value: LocalDate? = null,
    label: String? = null,
    isSecret: Boolean = false
) : InputViewBackend<LocalDate, DateInputViewBackend>(
    value, label, isSecret
), PopupSourceViewBackend {

    val dateInputTheme = DateInputTheme.default

}