package `fun`.adaptive.ui.input.integer

import `fun`.adaptive.ui.input.InputViewBackendBuilder

class IntInputViewBackendBuilder() : InputViewBackendBuilder<Int, IntInputViewBackend>() {

    var radix : Int? = null
    var showRadix10 : Boolean? = null
    var unit : String? = null

    override fun toBackend() =
        IntInputViewBackend(inputValue, label, secret).also { backend ->
            setup(backend)
            radix?.let { backend.radix = it }
            showRadix10?.let { backend.showRadix10 = it }
            unit?.let { backend.unit = it }
        }

}