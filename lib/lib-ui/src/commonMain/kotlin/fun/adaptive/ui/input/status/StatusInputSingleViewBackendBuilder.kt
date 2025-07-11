package `fun`.adaptive.ui.input.status

import `fun`.adaptive.ui.input.InputViewBackendBuilder
import `fun`.adaptive.ui.input.bool.BooleanInputTheme
import `fun`.adaptive.value.AvStatus

class StatusInputSingleViewBackendBuilder(
    var status : AvStatus
) : InputViewBackendBuilder<Set<AvStatus>, StatusInputSingleViewBackend>() {

    var booleanInputTheme : BooleanInputTheme? = null

    override fun toBackend() =
        StatusInputSingleViewBackend(status, inputValue, label, secret).also { backend ->
            setup(backend)
            backend.status = this.status
            booleanInputTheme?.let { backend.booleanInputTheme = it }
        }

}