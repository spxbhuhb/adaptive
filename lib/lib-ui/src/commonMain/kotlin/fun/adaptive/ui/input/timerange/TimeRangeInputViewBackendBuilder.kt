package `fun`.adaptive.ui.input.timerange

import `fun`.adaptive.lib.util.datetime.TimeRange
import `fun`.adaptive.ui.input.InputViewBackendBuilder

class TimeRangeInputViewBackendBuilder(
    inputValue: TimeRange?
) : InputViewBackendBuilder<TimeRange, TimeRangeInputViewBackend>(inputValue) {

    override fun toBackend() =
        TimeRangeInputViewBackend(inputValue, label, secret).also { backend ->
            setup(backend)
        }

}