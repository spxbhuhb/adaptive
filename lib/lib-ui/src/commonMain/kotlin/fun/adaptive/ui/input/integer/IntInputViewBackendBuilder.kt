package `fun`.adaptive.ui.input.integer

import `fun`.adaptive.ui.input.InputViewBackendBuilder

class IntInputViewBackendBuilder(
    inputValue: Int?
) : InputViewBackendBuilder<Int, IntInputViewBackend>(inputValue) {

    override fun toBackend() =
        IntInputViewBackend(inputValue, label, secret).also { backend ->
            setup(backend)
        }

}