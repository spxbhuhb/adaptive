package `fun`.adaptive.ui.editor

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.form.FormViewBackend.Companion.viewBackendFor
import `fun`.adaptive.ui.input.time.TimeInputViewBackend
import `fun`.adaptive.ui.input.time.timeInput
import kotlinx.datetime.LocalTime

@Adaptive
fun timeEditor(
    binding: AdaptiveStateVariableBinding<LocalTime>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> LocalTime?,
) : AdaptiveFragment {
    timeInput(
        fragment().viewBackendFor(binding) { value, label, isSecret ->
            TimeInputViewBackend(value, label, isSecret)
        }
    ) .. instructions()
    return fragment()
}