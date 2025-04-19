package `fun`.adaptive.ui.input.number

fun doubleInputBackend(inputValue : Double? = null, builder: DoubleInputViewBackendBuilder.() -> Unit = {  }) =
    DoubleInputViewBackendBuilder(inputValue).apply(builder).toBackend()
