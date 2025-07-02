package `fun`.adaptive.ui.input.timerange

import `fun`.adaptive.lib.util.datetime.TimeRange
import `fun`.adaptive.ui.fragment.structural.PopupSourceViewBackend
import `fun`.adaptive.ui.input.InputViewBackend

class TimeRangeInputViewBackend(
    value: TimeRange? = null,
    label: String? = null,
    isSecret: Boolean = false
) : InputViewBackend<TimeRange, TimeRangeInputViewBackend>(
    value, label, isSecret
), PopupSourceViewBackend