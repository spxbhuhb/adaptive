package `fun`.adaptive.ui.editor

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.resource.resolve.resolveStringOrNull
import `fun`.adaptive.ui.form.FormViewBackend.Companion.viewBackendFor
import `fun`.adaptive.ui.input.select.SingleSelectInputViewBackend
import `fun`.adaptive.ui.input.select.item.selectInputOptionText
import `fun`.adaptive.ui.input.select.item.selectInputValueText
import `fun`.adaptive.ui.input.select.mapping.RefMapInputMapping
import `fun`.adaptive.ui.input.select.selectInputDropdown
import `fun`.adaptive.value.AvRefLabel
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId

@Adaptive
fun <SPEC> refEditorNameDropdown(
    refLabel: AvRefLabel,
    options: List<AvValue<SPEC>>?,
    binding: AdaptiveStateVariableBinding<Map<AvRefLabel, AvValueId>?>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> Map<AvRefLabel, AvValueId>?,
): AdaptiveFragment {

    selectInputDropdown(
        fragment().viewBackendFor(binding) { value, label, isSecret ->
            SingleSelectInputViewBackend<Map<AvRefLabel, AvValueId>, AvValueId, AvValue<SPEC>>(
                value,
                { it.uuid },
                RefMapInputMapping(refLabel),
                fragment().resolveStringOrNull(refLabel) ?: label,
                isSecret
            ).also {
                it.options = options ?: emptyList()
                it.withSurfaceContainer = true
            }
        },
        { selectInputOptionText(it) { it.option.nameLike } },
        { selectInputValueText(it) { it.option.nameLike } }
    ) .. instructions()

    return fragment()
}