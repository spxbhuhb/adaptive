package `fun`.adaptive.ui.input.select

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.decoratedInput

@Adaptive
fun <IT, OT> selectInput(
    viewBackend: SingleSelectInputViewBackend<IT, OT>,
    @Adaptive
    _fixme_option: (option: AbstractSelectInputViewBackend<IT, IT, OT>.SelectItem) -> Unit
): AdaptiveFragment {
    val focus = focus()
    val observed = valueFrom { viewBackend }

    decoratedInput(focus, observed) {
        if (viewBackend.withDropDown) {
            selectInputDropdown(focus, observed, _fixme_option) .. instructions()
        } else {
            selectInputList(focus, observed, _fixme_option) .. instructions()
        }
    }

    return fragment()
}

@Adaptive
fun <IT, OT> selectInputList(
    focus: Boolean,
    viewBackend: AbstractSelectInputViewBackend<IT, IT, OT>,
    @Adaptive
    _fixme_option: (option: AbstractSelectInputViewBackend<IT, IT, OT>.SelectItem) -> Unit
): AdaptiveFragment {
    column(instructions()) {
        viewBackend.optionListContainerInstructions(focus)

        onKeydown { event -> viewBackend.onListKeydown(event) }

        for (item in viewBackend.items) {
            _fixme_option(item)
        }
    }

    return fragment()
}

@Adaptive
fun <IT, OT> selectInputDropdown(
    focus: Boolean,
    viewBackend: AbstractSelectInputViewBackend<IT, IT, OT>,
    @Adaptive
    _fixme_option: (option: AbstractSelectInputViewBackend<IT, IT, OT>.SelectItem) -> Unit
): AdaptiveFragment {
    column(instructions()) {
        viewBackend.dropdownSelectedContainerInstructions(focus)

        onKeydown { event -> viewBackend.onDropdownSelectedKeydown(event) }

        box {
            if (viewBackend.selectedItems.isNotEmpty()) {
                _fixme_option(viewBackend.selectedItems.first())
            }
        }

        primaryPopup(viewBackend) { hide ->
            column {
                viewBackend.listInputTheme.dropdownOptionsContainer

                onKeydown { event -> viewBackend.onListKeydown(event, hide) }

                for (item in viewBackend.items) {
                    _fixme_option(item)
                }
            }
        }
    }

    return fragment()
}