package `fun`.adaptive.ui.editor

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.form.FormViewBackend.Companion.viewBackendFor
import `fun`.adaptive.ui.input.color.ColorInputViewBackend
import `fun`.adaptive.ui.input.color.colorInput
import `fun`.adaptive.ui.instruction.decoration.Color

@Adaptive
fun colorEditor(
    binding: AdaptiveStateVariableBinding<Color>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> Color?,
) : AdaptiveFragment {
    colorInput(
        fragment().viewBackendFor(binding) { value, label, isSecret ->
            ColorInputViewBackend(value, label, isSecret)
        }
    ) .. instructions()
    return fragment()
}