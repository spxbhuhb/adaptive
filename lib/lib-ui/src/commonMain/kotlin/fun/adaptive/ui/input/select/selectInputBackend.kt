package `fun`.adaptive.ui.input.select

fun <T> selectInputBackend(inputValue: T? = null, builder: SelectInputViewBackendBuilder<T>.() -> Unit = { }) =
    SelectInputViewBackendBuilder(inputValue).apply(builder).toBackend()