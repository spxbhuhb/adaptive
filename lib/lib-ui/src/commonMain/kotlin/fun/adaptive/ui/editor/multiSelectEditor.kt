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
import `fun`.adaptive.ui.input.select.mapping.IdentityMapping
import `fun`.adaptive.ui.input.select.multiSelectInput

@Adaptive
fun <T> multiSelectEditor(
    options : List<T>,
    @Adaptive
    _fixme_itemFun : (AbstractSelectInputViewBackend<Set<T>,T,T>.SelectItem) -> Unit,
    binding: AdaptiveStateVariableBinding<Set<T>>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> Set<T>?
) : AdaptiveFragment {

    multiSelectInput(
        fragment().viewBackendFor(binding) { value, label, isSecret ->
            MultiSelectInputViewBackend(value, IdentityMapping(), label, isSecret).also {
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