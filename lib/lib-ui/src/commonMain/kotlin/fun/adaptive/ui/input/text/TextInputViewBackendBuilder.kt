package `fun`.adaptive.ui.input.text

import `fun`.adaptive.ui.input.InputViewBackendBuilder

class TextInputViewBackendBuilder(
    inputValue: String?
) : InputViewBackendBuilder<String, TextInputViewBackend>(inputValue) {

    var decimals : Int? = null

    override fun toBackend() =
        TextInputViewBackend(inputValue, label, secret).also { backend ->
            setup(backend)
        }

}