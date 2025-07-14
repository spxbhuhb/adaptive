package `fun`.adaptive.ui.editor

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.form.FormViewBackend.Companion.viewBackendFor
import `fun`.adaptive.ui.input.select.AbstractSelectInputViewBackend
import `fun`.adaptive.ui.input.select.SingleSelectInputViewBackend
import `fun`.adaptive.ui.input.select.mapping.IdentityInputMapping
import `fun`.adaptive.ui.input.select.mapping.SelectOptionMapping
import `fun`.adaptive.ui.input.select.selectInputList

@Adaptive
fun <VT, OT> selectMappingEditorList(
    options : List<OT>,
    mapping : SelectOptionMapping<VT,OT>,
    @Adaptive
    _fixme_itemFun : (AbstractSelectInputViewBackend<VT,VT,OT>.SelectItem) -> Unit,
    binding: AdaptiveStateVariableBinding<VT>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> VT?
) : AdaptiveFragment {

    selectInputList(
        fragment().viewBackendFor(binding) { value, label, isSecret ->
            SingleSelectInputViewBackend(
                value,
                mapping,
                IdentityInputMapping(),
                label,
                isSecret
            ).also {
                it.options = options
                it.withSurfaceContainer = true
            }
        },
        _fixme_itemFun,
    ) .. instructions()

    return fragment()
}