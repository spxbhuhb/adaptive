package `fun`.adaptive.ui.input.number

import `fun`.adaptive.ui.input.InputViewBackendBuilder

class DoubleInputViewBackendBuilder(
    inputValue: Double?
) : InputViewBackendBuilder<Double, DoubleInputViewBackend>(inputValue) {

    var decimals : Int? = null

    override fun toBackend() =
        DoubleInputViewBackend(inputValue, label, secret).also { backend ->
            setup(backend)
            decimals?.let { backend.decimals = it }
        }

}