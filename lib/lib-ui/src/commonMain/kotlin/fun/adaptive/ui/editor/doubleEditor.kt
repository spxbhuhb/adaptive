package `fun`.adaptive.ui.editor

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.form.FormViewBackend.Companion.viewBackendFor
import `fun`.adaptive.ui.input.double_.DoubleInputViewBackend
import `fun`.adaptive.ui.input.double_.doubleInput

@Adaptive
fun doubleEditor(
    binding: AdaptiveStateVariableBinding<Double>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> Double?,
) : AdaptiveFragment {
    doubleInput(
        fragment().viewBackendFor(binding) { value, label, isSecret ->
            DoubleInputViewBackend(value, label, isSecret)
        }
    ) .. instructions()
    return fragment()
}