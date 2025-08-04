package `fun`.adaptive.ui.input.select

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.focus
import `fun`.adaptive.ui.api.onKeyDown
import `fun`.adaptive.ui.api.onPointerMove
import `fun`.adaptive.ui.input.decoratedInput

@Adaptive
fun <IT,OT> multiSelectInputList(
    viewBackend: MultiSelectInputViewBackend<IT,OT>,
    _fixme_option: @Adaptive (option: AbstractSelectInputViewBackend<Set<IT>,IT,OT>.SelectItem) -> Unit
) : AdaptiveFragment {
    val focus = focus()
    val observed = observe { viewBackend }

    decoratedInput(focus, observed) {
        column(instructions()) {
            viewBackend.optionListContainerInstructions(focus)

            onPointerMove { observed.onPointerMove() }
            onKeyDown { event -> viewBackend.onListKeydown(event) }

            for (item in observed.items) {
                _fixme_option(item)
            }
        }
    }

    return fragment()
}