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
import `fun`.adaptive.ui.input.select.mapping.IdentityMultiInputMapping
import `fun`.adaptive.ui.input.select.mapping.IdentityOptionMapping
import `fun`.adaptive.ui.input.select.multiSelectInputList

@Adaptive
fun <T> multiSelectEditorList(
    options : List<T>,
    @Adaptive
    _fixme_itemFun : (AbstractSelectInputViewBackend<Set<T>,T,T>.SelectItem) -> Unit,
    binding: AdaptiveStateVariableBinding<Set<T>>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> Set<T>?
) : AdaptiveFragment {

    multiSelectInputList(
        fragment().viewBackendFor(binding) { value, label, isSecret ->
            MultiSelectInputViewBackend(
                value,
                IdentityOptionMapping(),
                IdentityMultiInputMapping(),
                label,
                isSecret
            ).also {
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