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
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun colorInput(
    viewBackend: ColorInputViewBackend
): AdaptiveFragment {

    val observed = valueFrom { viewBackend }
    val focus = focus()
    val theme = observed.colorInputTheme

    val hexBackend = textInputBackend(observed.safeHex()) {
        onChange = viewBackend::fromString
        validateFun = { it != null && it.length >= 6 && Color.decodeFromHexOrNull(it) != null }
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

            textInput2(hexBackend) .. width { 120.dp }
        }
    }

    return fragment()
}