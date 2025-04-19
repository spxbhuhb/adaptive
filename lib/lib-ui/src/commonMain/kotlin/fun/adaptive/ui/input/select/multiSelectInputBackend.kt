package `fun`.adaptive.ui.input.select

fun <T> multiSelectInputBackend(inputValue: Set<T>? = null, builder: MultiSelectInputViewBackendBuilder<T>.() -> Unit = { }) =
    MultiSelectInputViewBackendBuilder(inputValue).apply(builder).toBackend()