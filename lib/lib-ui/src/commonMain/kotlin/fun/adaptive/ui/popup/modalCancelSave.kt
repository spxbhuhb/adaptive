package `fun`.adaptive.ui.popup

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.generated.resources.cancel
import `fun`.adaptive.ui.generated.resources.save
import `fun`.adaptive.ui.input.button.button
import `fun`.adaptive.ui.input.button.submitButton

@Adaptive
fun modalCancelSave(hide: () -> Unit, theme : PopupTheme = PopupTheme.default, save: () -> Unit) {
    row {
        theme.modalButtons

        button(Strings.cancel) .. onClick { hide() }
        submitButton(Strings.save) .. onClick { save(); hide(); }
    }
}