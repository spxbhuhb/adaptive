package `fun`.adaptive.ui.input.text

import `fun`.adaptive.ui.input.InputViewBackendBuilder

class TextInputViewBackendBuilder(
    inputValue: String?
) : InputViewBackendBuilder<String, TextInputViewBackend>(inputValue) {

    var multiline : Boolean? = null

    override fun toBackend() =
        TextInputViewBackend(inputValue, label, secret).also { backend ->
            setup(backend)
            multiline?.let { backend.multiline = it }
        }

}