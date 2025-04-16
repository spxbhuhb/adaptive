package `fun`.adaptive.ui.popup

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.wrap.wrapFromBottom
import `fun`.adaptive.ui.wrap.wrapFromTop

@Adaptive
fun modalPopup(
    title : String,
    hide: () -> Unit,
    @Adaptive
    _fixme_buttons : () -> Unit,
    theme : PopupTheme =  PopupTheme.default,
    @Adaptive
    _fixme_content : () -> Unit
) {

    column(instructions()) {
        theme.modalContainer

        wrapFromTop(theme.modalTitleHeight, { modalPopupTitle(title, theme, hide) }) {
            wrapFromBottom(theme.modalButtonsHeight, _fixme_buttons) {
                _fixme_content()
            }
        }
    }
}