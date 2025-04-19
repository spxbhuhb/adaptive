package `fun`.adaptive.ui.editor

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.form.FormViewBackend.Companion.viewBackendFor
import `fun`.adaptive.ui.input.bool.BooleanInputViewBackend
import `fun`.adaptive.ui.input.bool.booleanInput

@Adaptive
fun booleanEditor(
    binding: AdaptiveStateVariableBinding<Boolean>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> Boolean,
) : AdaptiveFragment {
    booleanInput(
        fragment().viewBackendFor(binding) { value, label, isSecret ->
            BooleanInputViewBackend(value, label, isSecret)
        }
    ) .. instructions()
    return fragment()
}