package `fun`.adaptive.ui.input.dpixel

import `fun`.adaptive.ui.input.InputViewBackendBuilder
import `fun`.adaptive.ui.instruction.DPixel

class DPixelInputViewBackendBuilder(
    inputValue: DPixel?
) : InputViewBackendBuilder<DPixel, DPixelInputViewBackend>(inputValue) {

    var decimals : Int? = null

    override fun toBackend() =
        DPixelInputViewBackend(inputValue, label, secret).also { backend ->
            setup(backend)
            decimals?.let { backend.decimals = it }
        }

}