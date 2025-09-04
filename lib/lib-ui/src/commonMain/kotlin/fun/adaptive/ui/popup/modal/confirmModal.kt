package `fun`.adaptive.ui.popup.modal

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.NonAdaptive
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.FrontendWorkspace
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.cancel
import `fun`.adaptive.ui.generated.resources.ok
import `fun`.adaptive.ui.input.button.button
import `fun`.adaptive.ui.input.button.submitButton
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.popup.PopupTheme
import `fun`.adaptive.ui.popup.modal.components.modalPopupTitle
import `fun`.adaptive.ui.support.UiClose
import `fun`.adaptive.ui.support.UiSave
import `fun`.adaptive.ui.wrap.wrapFromBottom
import `fun`.adaptive.ui.wrap.wrapFromTop

@NonAdaptive
fun openConfirmModal(
    workspace: FrontendWorkspace,
    title: String,
    message: String,
    cancelLabel: String = Strings.cancel,
    okLabel: String = Strings.ok,
    theme: PopupTheme = PopupTheme.default,
    save: UiSave
) {
    dialog(workspace, ConfirmModalViewBackend(title, message, theme, cancelLabel, okLabel, save), ::confirmModal)
}


class ConfirmModalViewBackend(
    val title: String,
    val message: String,
    val theme: PopupTheme,
    val cancelLabel: String,
    val okLabel: String,
    val save: UiSave
)

@Adaptive
fun confirmModal(
    viewBackend: ConfirmModalViewBackend,
    close: UiClose
) {
    val theme = viewBackend.theme

    column {
        theme.modalContainer

        wrapFromTop(theme.modalTitleHeight, { modalPopupTitle(viewBackend.title, close, theme) }) {
            wrapFromBottom(
                theme.modalButtonsHeight,
                {
                    row {
                        theme.modalButtonsConfirm

                        button(viewBackend.cancelLabel) .. onClick { close.uiClose() }
                        submitButton(viewBackend.okLabel) .. onClick { viewBackend.save.uiSave(close); close.uiClose() }
                    }
                },
                {
                    box {
                        width { 400.dp } .. padding { 24.dp }
                        text(viewBackend.message)
                    }
                }
            )
        }
    }
}