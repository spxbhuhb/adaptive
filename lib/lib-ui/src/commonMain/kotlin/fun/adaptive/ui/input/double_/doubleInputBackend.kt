package `fun`.adaptive.ui.input.double_

fun doubleInputBackend(inputValue : Double? = null, builder: DoubleInputViewBackendBuilder.() -> Unit = {  }) =
    DoubleInputViewBackendBuilder(inputValue).apply(builder).toBackend()
