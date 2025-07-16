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
import `fun`.adaptive.ui.input.select.mapping.SelectInputMapping
import `fun`.adaptive.ui.input.select.mapping.SelectOptionMapping
import `fun`.adaptive.ui.input.select.selectInputDropdown
import `fun`.adaptive.value.AvRefLabel
import `fun`.adaptive.value.AvValueId

private typealias ITEM_TYPE = AvValueId
private typealias VALUE_TYPE = Map<AvRefLabel, AvValueId>

@Adaptive
fun <OPTION_TYPE> refEditorDropdown(
    refLabel: AvRefLabel,
    options: List<OPTION_TYPE>,
    optionToItemMapping : SelectOptionMapping<ITEM_TYPE,OPTION_TYPE>,
    inputToItemMapping: SelectInputMapping<VALUE_TYPE, ITEM_TYPE>,
    @Adaptive
    _fixme_itemFun: (AbstractSelectInputViewBackend<VALUE_TYPE, ITEM_TYPE, OPTION_TYPE>.SelectItem) -> Unit,
    @Adaptive
    _fixme_valueFun: (AbstractSelectInputViewBackend<VALUE_TYPE, ITEM_TYPE, OPTION_TYPE>.SelectItem) -> Unit,
    binding: AdaptiveStateVariableBinding<VALUE_TYPE>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> VALUE_TYPE?
): AdaptiveFragment {

    selectInputDropdown(
        fragment().viewBackendFor(binding, refLabel) { value, label, isSecret ->
            SingleSelectInputViewBackend(
                value,
                optionToItemMapping,
                inputToItemMapping,
                fragment().resolveStringOrNull(refLabel) ?: refLabel,
                isSecret
            ).also {
                //it.options = options
                it.withSurfaceContainer = true
            }
        }.also { if (it.options !== options) it.options = options },
        _fixme_itemFun,
        _fixme_valueFun,
    ) .. instructions()

    return fragment()
}