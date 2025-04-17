package `fun`.adaptive.ui.input

fun <T> inputBackend(inputValue : T? = null, builder: InputViewBackendBuilder<T>.() -> Unit = {  }) =
    InputViewBackendBuilder(inputValue).apply(builder).toBackend()
