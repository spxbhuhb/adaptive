package `fun`.adaptive.ui.input.datetime

import `fun`.adaptive.ui.input.InputViewBackendBuilder
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

class DateTimeInputViewBackendBuilder(
    inputValue: LocalDateTime?
) : InputViewBackendBuilder<LocalDateTime, DateTimeInputViewBackend>(inputValue) {

    override fun toBackend() =
        DateTimeInputViewBackend(inputValue, label, secret).also { backend ->
            setup(backend)
        }

}