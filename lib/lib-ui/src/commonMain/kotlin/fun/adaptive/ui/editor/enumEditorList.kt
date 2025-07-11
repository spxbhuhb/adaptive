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
import `fun`.adaptive.ui.input.select.item.selectInputOptionCheckbox
import `fun`.adaptive.ui.input.select.item.selectInputOptionIconAndText
import `fun`.adaptive.ui.input.select.item.selectInputValueIconAndText
import `fun`.adaptive.ui.input.select.mapping.IdentityMapping
import `fun`.adaptive.ui.input.select.selectInputDropdown
import `fun`.adaptive.ui.input.select.selectInputList
import kotlin.enums.EnumEntries

@Adaptive
fun <E : Enum<E>> enumEditorList(
    options : List<E>,
    binding: AdaptiveStateVariableBinding<E>? = null,
    @Suppress("unused")
    @PropertySelector
    selector: () -> E?
) : AdaptiveFragment {

    selectInputList(
        fragment().viewBackendFor(binding) { value, label, isSecret ->
            SingleSelectInputViewBackend(value, IdentityMapping(), label, isSecret).also {
                it.options = options
                it.withSurfaceContainer = true
            }
        },
    ) { selectInputOptionCheckbox(it) } .. instructions()

    return fragment()
}