package `fun`.adaptive.ui.editor

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.form.FormViewBackend.Companion.viewBackendFor
import `fun`.adaptive.ui.input.date.DateInputViewBackend
import `fun`.adaptive.ui.input.date.dateInput
import kotlinx.datetime.LocalDate

@Adaptive
fun dateEditor(
    binding: AdaptiveStateVariableBinding<LocalDate>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> LocalDate?,
) : AdaptiveFragment {
    dateInput(
        fragment().viewBackendFor(binding) { value, label, isSecret ->
            DateInputViewBackend(value, label, isSecret)
        }
    ) .. instructions()
    return fragment()
}