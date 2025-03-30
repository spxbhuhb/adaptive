package `fun`.adaptive.app.ws.auth.account

import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.*
import `fun`.adaptive.auth.api.AuthPrincipalApi
import `fun`.adaptive.auth.model.Credential
import `fun`.adaptive.auth.model.CredentialType
import `fun`.adaptive.document.ui.direct.h3
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.input.text.textInput
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.label.uuidLabel
import `fun`.adaptive.ui.label.withLabel
import `fun`.adaptive.ui.snackbar.infoNotification
import `fun`.adaptive.ui.snackbar.successNotification
import `fun`.adaptive.ui.snackbar.warningNotification
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.value.AvValueId
import kotlinx.datetime.Clock.System.now

@Adaptive
fun changePasswordPopup(
    principalId: AvValueId,
    hide: () -> Unit
) {
    var inputState = InputContext()

    var currentPassword: String = ""
    var newPassword: String = ""
    var confirmPassword: String = ""

    val workspace = fragment().firstContext<Workspace>()

    column {
        gap { 16.dp }

        box {
            column {
                h3(Strings.passwordChange)
                uuidLabel { principalId }
            }
        }

        withLabel(Strings.currentPassword) { s ->
            textInput("") { v -> currentPassword = v } .. secret
        }

        withLabel(Strings.newPassword, inputState) { s ->
            textInput("", s) { v -> newPassword = v } .. secret
        }

        withLabel(Strings.confirmPassword, inputState) { s ->
            textInput("", s) { v -> confirmPassword = v; } .. secret
        }

        button(Strings.save) .. alignSelf.end .. onClick {

            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                warningNotification(Strings.invalidFields)
                return@onClick
            }

            if (newPassword != confirmPassword) {
                warningNotification(Strings.invalidFields)
                return@onClick
            }

            workspace.io {
                val service = getService<AuthPrincipalApi>(workspace.transport)

                service.addCredential(
                    principalId,
                    Credential(CredentialType.PASSWORD, newPassword, now()),
                    Credential(CredentialType.PASSWORD, currentPassword, now())
                )

                successNotification(Strings.saveSuccess)
            }

            hide()
        }
    }
}