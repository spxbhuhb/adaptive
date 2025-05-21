package `fun`.adaptive.ui.editor

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.form.FormViewBackend.Companion.viewBackendFor
import `fun`.adaptive.ui.input.dpixel.DPixelInputViewBackend
import `fun`.adaptive.ui.input.dpixel.dPixelInput
import `fun`.adaptive.ui.instruction.DPixel

@Adaptive
fun dPixelEditor(
    binding: AdaptiveStateVariableBinding<DPixel>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> DPixel?,
) : AdaptiveFragment {
    dPixelInput(
        fragment().viewBackendFor(binding) { value, label, isSecret ->
            DPixelInputViewBackend(value, label, isSecret)
        }
    ) .. instructions()
    return fragment()
}