package `fun`.adaptive.ui.input.double_

import `fun`.adaptive.ui.input.InputViewBackendBuilder

class DoubleInputViewBackendBuilder(
    inputValue: Double?
) : InputViewBackendBuilder<Double, DoubleInputViewBackend>(inputValue) {

    var decimals : Int? = null
    var unit : String? = null

    override fun toBackend() =
        DoubleInputViewBackend(inputValue, label, secret).also { backend ->
            setup(backend)
            decimals?.let { backend.decimals = it }
            unit?.let { backend.unit = it }
        }

}