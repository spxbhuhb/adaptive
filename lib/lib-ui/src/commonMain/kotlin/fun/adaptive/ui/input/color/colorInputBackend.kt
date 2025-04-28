package `fun`.adaptive.ui.input.color

import `fun`.adaptive.ui.instruction.decoration.Color

fun colorInputBackend(inputValue : Color? = null, builder: ColorInputViewBackendBuilder.() -> Unit = {  }) =
    ColorInputViewBackendBuilder(inputValue).apply(builder).toBackend()
