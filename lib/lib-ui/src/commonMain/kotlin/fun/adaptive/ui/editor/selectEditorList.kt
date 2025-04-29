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
import `fun`.adaptive.ui.input.select.mapping.IdentityMapping
import `fun`.adaptive.ui.input.select.selectInputList

@Adaptive
fun <T> selectEditorList(
    options : List<T>,
    @Adaptive
    _fixme_itemFun : (AbstractSelectInputViewBackend<T,T,T>.SelectItem) -> Unit,
    binding: AdaptiveStateVariableBinding<T>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> T?
) : AdaptiveFragment {

    selectInputList(
        fragment().viewBackendFor(binding) { value, label, isSecret ->
            SingleSelectInputViewBackend(value, IdentityMapping(), label, isSecret).also {
                it.options = options
                it.withSurfaceContainer = true
            }
        },
        _fixme_itemFun,
    ) .. instructions()

    return fragment()
}