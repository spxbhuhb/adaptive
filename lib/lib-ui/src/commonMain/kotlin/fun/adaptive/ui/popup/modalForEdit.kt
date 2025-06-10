package `fun`.adaptive.ui.popup

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.wrap.wrapFromBottom
import `fun`.adaptive.ui.wrap.wrapFromTop

@Adaptive
fun modalForEdit(
    title : String,
    hide: () -> Unit,
    save: (hide : () -> Unit) -> Unit,
    theme : PopupTheme =  PopupTheme.default,
    @Adaptive
    _fixme_content : () -> Unit
) {

    column {
        theme.modalContainer

        wrapFromTop(theme.modalTitleHeight, { modalPopupTitle(title, theme, hide) }) {
            wrapFromBottom(theme.modalButtonsHeight, { modalCancelSave(hide, save = save) }) {
                _fixme_content()
            }
        }
    }
}