package `fun`.adaptive.ui.popup.modal.components

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.generated.resources.cancel
import `fun`.adaptive.ui.generated.resources.save
import `fun`.adaptive.ui.input.button.button
import `fun`.adaptive.ui.input.button.submitButton
import `fun`.adaptive.ui.popup.PopupTheme
import `fun`.adaptive.ui.support.UiClose
import `fun`.adaptive.ui.support.UiSave

@Adaptive
fun modalCancelSave(
    close: UiClose? = null,
    theme : PopupTheme = PopupTheme.Companion.default,
    save: UiSave? = null
) {
    row {
        theme.modalButtons

        button(Strings.cancel) .. onClick { (close ?: fragment().firstContext<UiClose>()).uiClose() }
        submitButton(Strings.save) .. onClick { (save ?: fragment().firstContext<UiSave>()).uiSave(close ?: fragment().firstContext<UiClose>()) }
    }
}