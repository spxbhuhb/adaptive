package `fun`.adaptive.ui.input.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

fun dateTimeInputBackend(inputValue : LocalDateTime? = null, builder: DateTimeInputViewBackendBuilder.() -> Unit = {  }) =
    DateTimeInputViewBackendBuilder(inputValue).apply(builder).toBackend()
