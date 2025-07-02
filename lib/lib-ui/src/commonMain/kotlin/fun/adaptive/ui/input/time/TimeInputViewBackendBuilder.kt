package `fun`.adaptive.ui.input.time

import `fun`.adaptive.ui.input.InputViewBackendBuilder
import kotlinx.datetime.LocalTime

class TimeInputViewBackendBuilder(
    inputValue: LocalTime?
) : InputViewBackendBuilder<LocalTime, TimeInputViewBackend>(inputValue) {

    override fun toBackend() =
        TimeInputViewBackend(inputValue, label, secret).also { backend ->
            setup(backend)
        }

}