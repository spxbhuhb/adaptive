package `fun`.adaptive.ui.input.integer

import `fun`.adaptive.ui.input.InputViewBackendBuilder

class IntInputViewBackendBuilder(
    inputValue: Int?
) : InputViewBackendBuilder<Int, IntInputViewBackend>(inputValue) {

    val radix : Int? = null
    val showRadix10 : Boolean? = null

    override fun toBackend() =
        IntInputViewBackend(inputValue, label, secret).also { backend ->
            setup(backend)
            radix?.let { backend.radix = it }
            showRadix10?.let { backend.showRadix10 = it }
        }

}