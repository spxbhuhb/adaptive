package `fun`.adaptive.ui.input.double_

fun doubleInputBackend(inputValue : Double? = null, builder: DoubleInputViewBackendBuilder.() -> Unit = {  }) =
    DoubleInputViewBackendBuilder().also { it.inputValue = inputValue}.apply(builder).toBackend()
