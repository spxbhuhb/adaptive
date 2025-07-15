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
import `fun`.adaptive.ui.input.select.mapping.IdentityOptionMapping
import `fun`.adaptive.ui.input.select.selectInputDropdown

@Adaptive
fun <T> selectEditorDropdown(
    options : List<T>,
    @Adaptive
    _fixme_itemFun : (AbstractSelectInputViewBackend<T,T,T>.SelectItem) -> Unit,
    @Adaptive
    _fixme_valueFun : (AbstractSelectInputViewBackend<T,T,T>.SelectItem) -> Unit,
    binding: AdaptiveStateVariableBinding<T>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> T?
) : AdaptiveFragment {

    selectInputDropdown(
        fragment().viewBackendFor(binding) { value, label, isSecret ->
            SingleSelectInputViewBackend(
                value,
                IdentityOptionMapping(),
                IdentityInputMapping(),
                label,
                isSecret
            ).also {
                // it.options = options
                it.withSurfaceContainer = true
            }
        }.also { if (it.options !== options) it.options = options },
        _fixme_itemFun,
        _fixme_valueFun,
    ) .. instructions()

    return fragment()
}