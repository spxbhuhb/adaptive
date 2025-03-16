package `fun`.adaptive.ui.input.datetime

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.dropShadow
import `fun`.adaptive.ui.api.focus
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.popupAlign
import `fun`.adaptive.ui.api.primaryPopup
import `fun`.adaptive.ui.api.tabIndex
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.api.zIndex
import `fun`.adaptive.ui.datetime.datePicker
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.input.InputTheme
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.utility.localDate
import kotlinx.datetime.LocalDate

@Adaptive
fun dateInput(
    value: LocalDate = localDate(),
    state: InputContext = InputContext(),
    theme: InputTheme = InputTheme.DEFAULT,
    onChange: (date: LocalDate) -> Unit
): AdaptiveFragment {

    val observed = valueFrom { state }
    val focus = focus()

    val themeInstructions = when {
        observed.disabled -> theme.disabled
        observed.invalid -> if (focus || observed.popupOpen) theme.invalidFocused else theme.invalidNotFocused
        else -> if (focus || observed.popupOpen) theme.focused else theme.enabled
    }

    box(themeInstructions, instructions()) {
        tabIndex { 0 }

        text(value.toString()) .. width { 200.dp } .. alignSelf.startCenter

        if (! observed.disabled) {

            primaryPopup(state) { hide ->
                popupAlign.belowStart .. onClick { it.stopPropagation() } .. zIndex { 10000 } .. padding { 4.dp }
                dropShadow(colors.reverse.opaque(0.1f), 2.dp, 2.dp, 2.dp)

                datePicker(value, hide, onChange)
            }
        }
    }

    return fragment()
}