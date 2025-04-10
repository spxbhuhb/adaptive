package `fun`.adaptive.ui.popup

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.tabIndex
import `fun`.adaptive.ui.builtin.cancel
import `fun`.adaptive.ui.builtin.save
import `fun`.adaptive.ui.button.ButtonTheme
import `fun`.adaptive.ui.button.button

@Adaptive
fun modalCancelSave(hide: () -> Unit, theme : PopupTheme = PopupTheme.default, save: () -> Unit) {
    row {
        theme.modalButtons

        button(Strings.cancel, theme = ButtonTheme.noFocus) .. onClick { hide() } .. tabIndex { 0 }
        button(Strings.save) .. onClick { save() } .. tabIndex { 0 }
    }
}