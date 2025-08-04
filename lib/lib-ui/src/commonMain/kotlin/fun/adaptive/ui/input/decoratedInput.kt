package `fun`.adaptive.ui.input

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.help
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.textSmall
import `fun`.adaptive.utility.debug

@Adaptive
fun <T> decoratedInput(
    focused: Boolean,
    viewBackend: InputViewBackend<T, *>,
    _KT_74337_content: @Adaptive (InputViewBackend<T, *>) -> Unit
): AdaptiveFragment {

    if (viewBackend.label?.isNotEmpty() == true) {
        withLabel(focused, viewBackend) { _KT_74337_content(viewBackend) } .. instructions()
    } else {
        column {
            instructions()
            _KT_74337_content(viewBackend)
        }
    }

    return fragment()
}

@Adaptive
private fun <T> withLabel(
    focused: Boolean,
    viewBackend: InputViewBackend<T, *>,
    _KT_74337_content: @Adaptive (InputViewBackend<T, *>) -> Unit
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
                text(config.text, config.instruction) .. viewBackend.labelTheme.rightLabel
            }
        }

        InputViewBackend.LabelPosition.Top -> {
            column(instructions()) {
                fillStrategy.constrain
                row {
                    gap { 8.dp }

                    text(config.text, config.instruction)

                    if (viewBackend.help != null) {
                        icon(Graphics.help, theme = viewBackend.labelTheme.infoIconTheme)
                        primaryPopup {
                            viewBackend.labelTheme.infoPopup
                            text(viewBackend.help) .. textSmall
                        }
                    }
                }
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