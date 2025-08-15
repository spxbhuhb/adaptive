package `fun`.adaptive.ui.popup.modal

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.generated.resources.ok
import `fun`.adaptive.ui.input.button.button
import `fun`.adaptive.ui.input.button.submitButton
import `fun`.adaptive.ui.popup.PopupTheme
import `fun`.adaptive.ui.popup.modal.components.modalCancelOk
import `fun`.adaptive.ui.popup.modal.components.modalPopupTitle
import `fun`.adaptive.ui.support.UiClose
import `fun`.adaptive.ui.support.UiSave
import `fun`.adaptive.ui.wrap.wrapFromBottom
import `fun`.adaptive.ui.wrap.wrapFromTop

@Adaptive
fun okModal(
    title: String,
    close: UiClose? = null,
    theme: PopupTheme = PopupTheme.default,
    content: @Adaptive () -> Unit
): AdaptiveFragment {

    column(instructions()) {
        theme.modalContainer

        wrapFromTop(theme.modalTitleHeight, { modalPopupTitle(title, close, theme) }) {
            wrapFromBottom(
                theme.modalButtonsHeight,
                {
                    row {
                        theme.modalButtons
                        submitButton(Strings.ok) .. onClick { close?.uiClose() }
                    }
                }
            ) {
                content()
            }
        }
    }

    return fragment()
}