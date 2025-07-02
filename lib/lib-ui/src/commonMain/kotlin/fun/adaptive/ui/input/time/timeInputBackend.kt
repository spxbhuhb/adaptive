package `fun`.adaptive.ui.input.time

import kotlinx.datetime.LocalTime

fun timeInputBackend(inputValue : LocalTime? = null, builder: TimeInputViewBackendBuilder.() -> Unit = {  }) =
    TimeInputViewBackendBuilder(inputValue).apply(builder).toBackend()
