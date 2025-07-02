package `fun`.adaptive.ui.input.time

import `fun`.adaptive.ui.fragment.structural.PopupSourceViewBackend
import `fun`.adaptive.ui.input.InputViewBackend
import kotlinx.datetime.LocalTime

class TimeInputViewBackend(
    value: LocalTime? = null,
    label: String? = null,
    isSecret: Boolean = false
) : InputViewBackend<LocalTime, TimeInputViewBackend>(
    value, label, isSecret
), PopupSourceViewBackend {

    val timeInputTheme = TimeInputTheme.default

}