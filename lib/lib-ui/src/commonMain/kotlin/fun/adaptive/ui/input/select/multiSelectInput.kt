package `fun`.adaptive.ui.input.select

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.focus
import `fun`.adaptive.ui.api.onKeydown
import `fun`.adaptive.ui.input.decoratedInput

@Adaptive
fun <IT,OT> multiSelectInput(
    viewBackend: MultiSelectInputViewBackend<IT,OT>,
    @Adaptive
    _fixme_option: (option: AbstractSelectInputViewBackend<Set<IT>,IT,OT>.SelectItem) -> Unit
) : AdaptiveFragment {
    val focus = focus()
    val observed = valueFrom { viewBackend }

    decoratedInput(focus, observed) {
        column(instructions()) {
            viewBackend.optionListContainerInstructions(focus)

            onKeydown { event -> viewBackend.onListKeydown(event) }

            for (item in observed.items) {
                _fixme_option(item)
            }
        }
    }

    return fragment()
}