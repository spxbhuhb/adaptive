package `fun`.adaptive.ui.input.status

import `fun`.adaptive.ui.input.InputViewBackend
import `fun`.adaptive.ui.input.bool.BooleanInputTheme
import `fun`.adaptive.ui.instruction.layout.PopupAlign
import `fun`.adaptive.value.AvStatus
import kotlin.properties.Delegates.observable

class StatusInputSingleViewBackend(
    var status : AvStatus,
    value: Set<AvStatus>? = null,
    label: String? = status,
    isSecret: Boolean = false,
) : InputViewBackend<Set<AvStatus>, StatusInputSingleViewBackend>(
    value, label, isSecret
) {

    override var labelAlignment by observable(PopupAlign.afterCenter, ::notify)

    var booleanInputTheme = BooleanInputTheme.default

    fun toggle() {
        val safeInputValue = inputValue

        if (safeInputValue == null) {
            inputValue = setOf(status)
        } else {
            if (status in safeInputValue) {
                inputValue = safeInputValue - status
            } else {
                inputValue = safeInputValue + status
            }
        }
    }
}