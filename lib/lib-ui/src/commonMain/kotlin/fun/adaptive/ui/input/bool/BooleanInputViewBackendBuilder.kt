package `fun`.adaptive.ui.input.bool

import `fun`.adaptive.ui.input.InputViewBackendBuilder

class BooleanInputViewBackendBuilder(
    inputValue: Boolean?
) : InputViewBackendBuilder<Boolean, BooleanInputViewBackend>(inputValue) {

    var booleanInputTheme : BooleanInputTheme? = null

    override fun toBackend() =
        BooleanInputViewBackend(inputValue, label, secret).also { backend ->
            setup(backend)
            booleanInputTheme?.let { backend.booleanInputTheme = it }
        }

}