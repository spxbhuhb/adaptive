package `fun`.adaptive.app.ws.auth.account

import `fun`.adaptive.lib_app.generated.resources.*
import `fun`.adaptive.adat.api.update
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.app.ws.shared.wsContentHeader
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.button.ButtonTheme
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.input.text.textInput
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.label.withLabel
import `fun`.adaptive.ui.popup.PopupTheme
import `fun`.adaptive.ui.workspace.WorkspaceTheme
import `fun`.adaptive.utility.uppercaseFirstChar

@Adaptive
fun accountEditorSelf(
    account: AccountEditorData?,
    save: (AccountEditorData) -> Unit
) {
    val popupState = valueFrom { InputContext() }

    // This is to prevent Safari overwriting the e-mail field on password change.
    // With autofill, Safari just finds whatever field it likes and changes the value.
    // Nothing else matters, but the field being disabled. So, I disable the input fields
    // on the form when the password change popup is open. This strangely works.
    // I lost a few years of my life by being very-very angry while I figured this out.

    val safariHack = InputContext(disabled = popupState.value.popupOpen)

    var copy = copyOf { account ?: AccountEditorData() }

    column {
        WorkspaceTheme.DEFAULT.contentPaneContainer

        wsContentHeader(Strings.accountSelf, copy.principalId) {
            row {
                button(Strings.passwordChange, theme = ButtonTheme.noFocus)
                primaryPopup(popupState) { hide ->
                    PopupTheme.default.inlineEditorPopup .. width { 300.dp }
                    changePasswordPopup(copy.principalId!!, hide)
                }
            }
            button(Strings.save) .. onClick { save(copy) }
        }

        withLabel(Strings.accountName, InputContext(disabled = true)) { state ->
            width { 400.dp }
            textInput(copy.principalName, state) { }
        }

        withLabel(Strings.name) { state ->
            width { 400.dp }
            textInput(copy.name, safariHack) { copy.update(copy::name, it) }
        }

        withLabel(Strings.email.uppercaseFirstChar()) { state ->
            width { 400.dp }
            textInput(copy.email, safariHack) { copy.update(copy::email, it) }
        }
    }
}