package `fun`.adaptive.ui.input.timerange

import `fun`.adaptive.lib.util.datetime.TimeRange

fun timeRangeInputBackend(inputValue : TimeRange? = null, builder: TimeRangeInputViewBackendBuilder.() -> Unit = {  }) =
    TimeRangeInputViewBackendBuilder(inputValue).apply(builder).toBackend()
