package `fun`.adaptive.ui.input.date

import `fun`.adaptive.ui.input.InputViewBackendBuilder
import kotlinx.datetime.LocalDate

class DateInputViewBackendBuilder(
    inputValue: LocalDate?
) : InputViewBackendBuilder<LocalDate, DateInputViewBackend>(inputValue) {

    override fun toBackend() =
        DateInputViewBackend(inputValue, label, secret).also { backend ->
            setup(backend)
        }

}