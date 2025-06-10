package `fun`.adaptive.ui.editor

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.form.FormViewBackend.Companion.viewBackendFor
import `fun`.adaptive.ui.input.longint.LongInputViewBackend
import `fun`.adaptive.ui.input.longint.longInput

@Adaptive
fun longEditor(
    binding: AdaptiveStateVariableBinding<Long>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> Long?,
) : AdaptiveFragment {
    longInput(
        fragment().viewBackendFor(binding) { value, label, isSecret ->
            LongInputViewBackend(value, label, isSecret)
        }
    ) .. instructions()
    return fragment()
}