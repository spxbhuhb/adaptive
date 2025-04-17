package `fun`.adaptive.ui.editor

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.api.firstContextOrNull
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.form.FormViewBackend
import `fun`.adaptive.ui.input.InputViewBackend
import `fun`.adaptive.ui.input.text.textInput2

@Adaptive
fun textEditor(
    binding: AdaptiveStateVariableBinding<String>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> String,
) {
    val inputBackend = fragment().firstContextOrNull<FormViewBackend>()?.backendFor(binding) ?: InputViewBackend()
    textInput2(inputBackend)
}