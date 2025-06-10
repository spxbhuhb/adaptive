package `fun`.adaptive.ui.input.longint

import `fun`.adaptive.ui.input.InputViewBackendBuilder

class LongInputViewBackendBuilder(
    inputValue: Long?
) : InputViewBackendBuilder<Long, LongInputViewBackend>(inputValue) {

    override fun toBackend() =
        LongInputViewBackend(inputValue, label, secret).also { backend ->
            setup(backend)
        }

}