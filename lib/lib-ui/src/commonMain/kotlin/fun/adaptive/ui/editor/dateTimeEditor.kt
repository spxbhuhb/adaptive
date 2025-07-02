package `fun`.adaptive.ui.editor

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.form.FormViewBackend.Companion.viewBackendFor
import `fun`.adaptive.ui.input.datetime.DateTimeInputViewBackend
import `fun`.adaptive.ui.input.datetime.dateTimeInput
import kotlinx.datetime.LocalDateTime

@Adaptive
fun dateTimeEditor(
    binding: AdaptiveStateVariableBinding<LocalDateTime>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> LocalDateTime?,
) : AdaptiveFragment {
    dateTimeInput(
        fragment().viewBackendFor(binding) { value, label, isSecret ->
            DateTimeInputViewBackend(value, label, isSecret)
        }
    ) .. instructions()
    return fragment()
}