package `fun`.adaptive.ui.input.integer

fun intInputBackend(inputValue : Int? = null, builder: IntInputViewBackendBuilder.() -> Unit = {  }) =
    IntInputViewBackendBuilder(inputValue).apply(builder).toBackend()
