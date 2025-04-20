package `fun`.adaptive.ui.editor

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.form.FormViewBackend.Companion.viewBackendFor
import `fun`.adaptive.ui.input.select.AbstractSelectInputViewBackend
import `fun`.adaptive.ui.input.select.MultiSelectInputViewBackend
import `fun`.adaptive.ui.input.select.mapping.SelectOptionMapping
import `fun`.adaptive.ui.input.select.multiSelectInput

@Adaptive
fun <VT,OT> multiSelectMappingEditor(
    options : List<OT>,
    mapping : SelectOptionMapping<VT,OT>,
    @Adaptive
    _fixme_itemFun : (AbstractSelectInputViewBackend<Set<VT>,VT,OT>.SelectItem) -> Unit,
    binding: AdaptiveStateVariableBinding<Set<VT>>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> Set<VT>?
) : AdaptiveFragment {

    multiSelectInput(
        fragment().viewBackendFor(binding) { value, label, isSecret ->
            MultiSelectInputViewBackend(value, mapping, label, isSecret).also {
                it.withSurfaceContainer = true
                it.isMultiSelect = true
            }
        }.also {
            it.options = options
        },
        _fixme_itemFun,
    ) .. instructions()

    return fragment()
}