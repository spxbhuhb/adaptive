package `fun`.adaptive.ui.popup

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.support.UiClose
import `fun`.adaptive.ui.support.UiSave
import `fun`.adaptive.ui.wrap.wrapFromBottom
import `fun`.adaptive.ui.wrap.wrapFromTop

@Adaptive
fun modalForEdit(
    title : String,
    hide: UiClose? = null,
    save: UiSave? = null,
    theme : PopupTheme =  PopupTheme.default,
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