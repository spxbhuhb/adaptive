package `fun`.adaptive.ui.input.date

import kotlinx.datetime.LocalDate

fun dateInputBackend(inputValue : LocalDate? = null, builder: DateInputViewBackendBuilder.() -> Unit = {  }) =
    DateInputViewBackendBuilder(inputValue).apply(builder).toBackend()
