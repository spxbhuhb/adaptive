package `fun`.adaptive.ui.editor

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.form.FormViewBackend.Companion.viewBackendFor
import `fun`.adaptive.ui.input.select.SelectInputViewBackend
import `fun`.adaptive.ui.input.select.selectInput

@Adaptive
fun <T> selectEditor(
    options : List<T>,
    @Adaptive
    _fixme_itemFun : (SelectInputViewBackend.SelectItem<T>) -> Unit,
    binding: AdaptiveStateVariableBinding<T>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> T?
) : AdaptiveFragment {

    selectInput(
        fragment().viewBackendFor(binding) { value, label, isSecret ->
            SelectInputViewBackend(value, label, isSecret).also {
                it.options = options
                it.withSurfaceContainer = true
            }
        },
        _fixme_itemFun,
    ) .. instructions()

    return fragment()
}