package `fun`.adaptive.ui.popup.modal

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.popup.PopupTheme
import `fun`.adaptive.ui.popup.modal.components.modalCancelSave
import `fun`.adaptive.ui.popup.modal.components.modalPopupTitle
import `fun`.adaptive.ui.support.UiClose
import `fun`.adaptive.ui.support.UiSave
import `fun`.adaptive.ui.wrap.wrapFromBottom
import `fun`.adaptive.ui.wrap.wrapFromTop

@Adaptive
fun modalForEdit(
    title : String,
    hide: UiClose? = null,
    save: UiSave? = null,
    theme : PopupTheme =  PopupTheme.Companion.default,
    @Adaptive
    _fixme_content : () -> Unit
) {

    column {
        theme.modalContainer

        wrapFromTop(theme.modalTitleHeight, { modalPopupTitle(title, hide, theme) }) {
            wrapFromBottom(theme.modalButtonsHeight, { modalCancelSave(hide, save = save) }) {
                _fixme_content()
            }
        }
    }
}