package `fun`.adaptive.ui.input.select

fun <T> selectInputBackend(inputValue: T? = null, builder: SingleSelectInputViewBackendBuilder<T>.() -> Unit = { }) =
    SingleSelectInputViewBackendBuilder(inputValue).apply(builder).toBackend()