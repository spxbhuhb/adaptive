package `fun`.adaptive.ui.input.color

import `fun`.adaptive.ui.input.InputViewBackendBuilder
import `fun`.adaptive.ui.instruction.decoration.Color

class ColorInputViewBackendBuilder(
    inputValue: Color?
) : InputViewBackendBuilder<Color, ColorInputViewBackend>(inputValue) {

    var colorInputTheme : ColorInputTheme? = null

    override fun toBackend() =
        ColorInputViewBackend(inputValue, label, secret).also { backend ->
            setup(backend)
            colorInputTheme?.let { backend.colorInputTheme = it }
        }

}