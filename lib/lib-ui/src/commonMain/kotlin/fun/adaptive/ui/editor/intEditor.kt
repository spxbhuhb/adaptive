package `fun`.adaptive.ui.editor

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.form.FormViewBackend.Companion.viewBackendFor
import `fun`.adaptive.ui.input.integer.IntInputViewBackend
import `fun`.adaptive.ui.input.integer.intInput

@Adaptive
fun intEditor(
    binding: AdaptiveStateVariableBinding<Int>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> Int?,
) : AdaptiveFragment {
    intInput(
        fragment().viewBackendFor(binding) { value, label, isSecret ->
            IntInputViewBackend(value, label, isSecret)
        }
    ) .. instructions()
    return fragment()
}