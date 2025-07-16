package `fun`.adaptive.ui.editor

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.resource.resolve.resolveString
import `fun`.adaptive.resource.resolve.resolveStringOrNull
import `fun`.adaptive.ui.form.FormViewBackend.Companion.viewBackendFor
import `fun`.adaptive.ui.input.select.AbstractSelectInputViewBackend
import `fun`.adaptive.ui.input.select.SingleSelectInputViewBackend
import `fun`.adaptive.ui.input.select.item.selectInputOptionText
import `fun`.adaptive.ui.input.select.item.selectInputValueText
import `fun`.adaptive.ui.input.select.mapping.RefMapInputMapping
import `fun`.adaptive.ui.input.select.mapping.SelectInputMapping
import `fun`.adaptive.ui.input.select.mapping.SelectOptionMapping
import `fun`.adaptive.ui.input.select.selectInputDropdown
import `fun`.adaptive.value.AvRefLabel
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.util.PathAndValue

@Adaptive
fun <SPEC> refEditorPathDropdown(
    refLabel: AvRefLabel,
    options: List<PathAndValue<SPEC>>?,
    binding: AdaptiveStateVariableBinding<Map<AvRefLabel, AvValueId>?>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> Map<AvRefLabel, AvValueId>?,
): AdaptiveFragment {

    selectInputDropdown(
        fragment().viewBackendFor(binding, refLabel) { value, label, isSecret ->
            SingleSelectInputViewBackend<Map<AvRefLabel, AvValueId>, AvValueId, PathAndValue<SPEC>>(
                value,
                { it.value.uuid },
                RefMapInputMapping(refLabel),
                fragment().resolveStringOrNull(refLabel) ?: refLabel,
                isSecret
            ).also {
                // it.options = options
                it.withSurfaceContainer = true
            }
        }.also { if (it.options !== options) it.options = options ?: emptyList() },
        { selectInputOptionText(it) { path.reversed().joinToString(" / ") } },
        { selectInputValueText(it) { path.reversed().joinToString(" / ") } }
    ) .. instructions()

    return fragment()
}