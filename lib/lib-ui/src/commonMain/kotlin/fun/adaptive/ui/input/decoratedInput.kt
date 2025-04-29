package `fun`.adaptive.ui.input

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.fillStrategy
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text

@Adaptive
fun <T> decoratedInput(
    focused: Boolean,
    viewBackend: InputViewBackend<T, *>,
    @Adaptive
    _KT_74337_content: (InputViewBackend<T, *>) -> Unit
): AdaptiveFragment {

    if (viewBackend.label?.isNotEmpty() == true) {
        withLabel(focused, viewBackend) { _KT_74337_content(viewBackend) } .. instructions()
    } else {
        _KT_74337_content(viewBackend)
    }

    return fragment()
}

@Adaptive
private fun <T> withLabel(
    focused: Boolean,
    viewBackend: InputViewBackend<T, *>,
    @Adaptive
    _KT_74337_content: (InputViewBackend<T,*>) -> Unit
): AdaptiveFragment {

    val config = viewBackend.labelConfiguration(focused)

    when (config.labelPosition) {
        InputViewBackend.LabelPosition.Left -> {
            row(instructions()) {
                fillStrategy.constrain
                text(config.text, config.instruction)
                _KT_74337_content(viewBackend)
            }
        }

        InputViewBackend.LabelPosition.Right -> {
            row(instructions()) {
                fillStrategy.constrainReverse
                _KT_74337_content(viewBackend)
                text(config.text, config.instruction)
            }
        }

        InputViewBackend.LabelPosition.Top -> {
            column(instructions()) {
                fillStrategy.constrain
                text(config.text, config.instruction)
                _KT_74337_content(viewBackend)
            }
        }

        InputViewBackend.LabelPosition.Bottom -> {
            column(instructions()) {
                fillStrategy.constrainReverse
                _KT_74337_content(viewBackend)
                text(config.text, config.instruction)
            }
        }
    }

    return fragment()
}