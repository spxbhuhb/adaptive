package `fun`.adaptive.ui.popup.modal

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.popup.PopupTheme
import `fun`.adaptive.ui.popup.modal.components.modalCancelOk
import `fun`.adaptive.ui.popup.modal.components.modalPopupTitle
import `fun`.adaptive.ui.support.UiClose
import `fun`.adaptive.ui.support.UiSave
import `fun`.adaptive.ui.wrap.wrapFromBottom
import `fun`.adaptive.ui.wrap.wrapFromTop

@Adaptive
fun basicModal(
    title: String,
    buttons: @Adaptive ((close: UiClose?, theme: PopupTheme, save: UiSave?) -> Unit)? = null,
    close: UiClose? = null,
    save: UiSave? = null,
    theme: PopupTheme = PopupTheme.Companion.default,
    content: @Adaptive () -> Unit
): AdaptiveFragment {

    column(instructions()) {
        theme.modalContainer

        wrapFromTop(theme.modalTitleHeight, { modalPopupTitle(title, close, theme) }) {
            wrapFromBottom(
                theme.modalButtonsHeight,
                {
                    if (buttons == null) {
                        modalCancelOk(close, theme, save)
                    } else {
                        buttons(close, theme, save)
                    }
                }
            ) {
                content()
            }
        }
    }

    return fragment()
}