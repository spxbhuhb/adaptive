package `fun`.adaptive.ui.input.color

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.decoratedInput
import `fun`.adaptive.ui.input.text.textInput2
import `fun`.adaptive.ui.input.text.textInputBackend
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun colorInput(
    viewBackend: ColorInputViewBackend
): AdaptiveFragment {

    val observed = valueFrom { viewBackend }
    val focus = focus()
    val theme = observed.colorInputTheme

    val hexBackend = textInputBackend(viewBackend.safeHex()) {
        onChange = viewBackend::fromString
    }

    val exampleColor = observed.inputValue ?: colors.transparent

    decoratedInput(focus, observed) {
        row {
            theme.inputContainer .. instructions()

            box {
                theme.inputExample(exampleColor)

                primaryPopup(observed) { hide ->
                    colorPickerPopup(observed, hide)
                }
            }

            textInput2(hexBackend)
        }
    }

    return fragment()
}