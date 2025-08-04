package `fun`.adaptive.ui.popup.modal

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.generated.resources.save
import `fun`.adaptive.ui.popup.PopupTheme
import `fun`.adaptive.ui.popup.modal.components.modalCancelOk
import `fun`.adaptive.ui.popup.modal.components.modalPopupTitle
import `fun`.adaptive.ui.support.UiClose
import `fun`.adaptive.ui.support.UiSave
import `fun`.adaptive.ui.wrap.wrapFromBottom
import `fun`.adaptive.ui.wrap.wrapFromTop

@Adaptive
fun editorModal(
    title : String,
    hide: UiClose? = null,
    save: UiSave? = null,
    theme : PopupTheme =  PopupTheme.Companion.default,
    content : @Adaptive () -> Unit
) {

    column {
        theme.modalContainer

        wrapFromTop(theme.modalTitleHeight, { modalPopupTitle(title, hide, theme) }) {
            wrapFromBottom(theme.modalButtonsHeight, { modalCancelOk(hide, save = save, okLabel = Strings.save) }) {
                content()
            }
        }
    }
}