package `fun`.adaptive.app.ws.auth.account

import `fun`.adaptive.app.ws.shared.wsContentHeader
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.lib_app.generated.resources.accountSelf
import `fun`.adaptive.lib_app.generated.resources.passwordChange
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.button.ButtonTheme
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.button.saveFormButton
import `fun`.adaptive.ui.editor.textEditor
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.popup.PopupTheme
import `fun`.adaptive.ui.workspace.WorkspaceTheme

@Adaptive
fun accountEditorSelf(
    account: AccountEditorData,
    save: (AccountEditorData) -> Unit
) {
    val form = adatFormBackend(account) {
        expectEquals(it::password, it::confirmPassword, dualTouch = true)
    }

    // This is to prevent Safari overwriting the e-mail field on password change.
    // With autofill, Safari just finds whatever field it likes and changes the value.
    // Nothing else matters, but the field being disabled. So, I disable the input fields
    // on the form when the password change popup is open. This strangely works.
    // I lost a few years of my life by being very-very angry while I figured this out.

    val popupState = valueFrom { InputContext() }.also {
        if (it.value.isPopupOpen) {
            form.disableAll()
        } else {
            form.enableAll()
        }
    }

    column {
        WorkspaceTheme.DEFAULT.contentPaneContainer

        wsContentHeader(Strings.accountSelf, account.principalId) {
            row {
                button(Strings.passwordChange, theme = ButtonTheme.noFocus)
                primaryPopup(popupState) { hide ->
                    PopupTheme.default.inlineEditorPopup .. width { 300.dp }
                    changePasswordPopup(account.principalId !!, hide)
                }
            }
            saveFormButton(form) { save(it) }
        }

        localContext(form) {
            column {
                width { 320.dp } .. gap { 16.dp }
                textEditor { account.principalName }
                textEditor { account.name }
                textEditor { account.email }
            }
        }
    }
}