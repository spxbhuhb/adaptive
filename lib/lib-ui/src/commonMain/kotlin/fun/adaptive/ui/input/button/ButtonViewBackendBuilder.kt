package `fun`.adaptive.ui.input.button

import `fun`.adaptive.ui.input.InputViewBackendBuilder

class ButtonViewBackendBuilder : InputViewBackendBuilder<Unit, ButtonViewBackend>(null) {

    var buttonTheme: ButtonTheme? = null

    override fun toBackend() =
        ButtonViewBackend(label).also { backend ->
            setup(backend)
            buttonTheme?.let { backend.buttonTheme = it }
        }

}