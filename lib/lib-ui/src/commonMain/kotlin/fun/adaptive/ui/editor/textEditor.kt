package `fun`.adaptive.ui.editor

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.form.FormViewBackend.Companion.viewBackendFor
import `fun`.adaptive.ui.input.text.TextInputViewBackend
import `fun`.adaptive.ui.input.text.textInput2

@Adaptive
fun textEditor(
    binding: AdaptiveStateVariableBinding<String>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> String?
): AdaptiveFragment {

    textInput2(
        fragment().viewBackendFor(binding) { value, label, isSecret ->
            TextInputViewBackend(value, label, isSecret)
        }
    ) .. instructions()

    return fragment()
}