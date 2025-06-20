package `fun`.adaptive.ui.input.select

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.focus
import `fun`.adaptive.ui.api.onKeydown
import `fun`.adaptive.ui.api.onMove
import `fun`.adaptive.ui.input.decoratedInput

@Adaptive
fun <IT, OT> selectInputList(
    viewBackend: AbstractSelectInputViewBackend<IT, IT, OT>,
    @Adaptive
    _fixme_option: (option: AbstractSelectInputViewBackend<IT, IT, OT>.SelectItem) -> Unit
): AdaptiveFragment {

    val focus = focus()
    val observed = observe { viewBackend }

    decoratedInput(focus, observed) {

        column(instructions()) {
            observed.optionListContainerInstructions(focus)

            onMove { observed.onPointerMove() }
            onKeydown { event -> observed.onListKeydown(event) }

            for (item in observed.items) {
                _fixme_option(item)
            }
        }
    }

    return fragment()
}