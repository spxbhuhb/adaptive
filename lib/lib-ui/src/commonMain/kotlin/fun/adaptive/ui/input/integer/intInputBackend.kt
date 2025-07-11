package `fun`.adaptive.ui.input.integer

fun intInputBackend(inputValue : Int? = null, builder: IntInputViewBackendBuilder.() -> Unit = {  }) =
    IntInputViewBackendBuilder().also { it.inputValue = inputValue}.apply(builder).toBackend()
