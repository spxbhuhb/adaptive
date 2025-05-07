package `fun`.adaptive.app.ws.auth.account

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.properties
import `fun`.adaptive.auth.api.AuthPrincipalApi
import `fun`.adaptive.auth.model.Credential
import `fun`.adaptive.auth.model.CredentialType
import `fun`.adaptive.document.ui.direct.h3
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.lib_app.generated.resources.passwordChange
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.button.button
import `fun`.adaptive.ui.editor.textEditor
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.generated.resources.save
import `fun`.adaptive.ui.generated.resources.saveSuccess
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.label.uuidLabel
import `fun`.adaptive.ui.snackbar.successNotification
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.value.AvValueId
import kotlinx.datetime.Clock.System.now

@Adat
class PasswordChange(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = ""
) {
    override fun descriptor() {
        properties {
            currentPassword blank false secret true
            newPassword blank false secret true
            confirmPassword blank false secret true
        }
    }
}

@Adaptive
fun changePasswordPopup(
    principalId: AvValueId,
    hide: () -> Unit
) {
    val template = PasswordChange()
    val form = adatFormBackend(template) {
        expectEquals(it::newPassword, it::confirmPassword, true)
    }
    val workspace = fragment().firstContext<Workspace>()

    column {
        gap { 16.dp }

        box {
            column {
                h3(Strings.passwordChange)
                uuidLabel { principalId }
            }
        }

        localContext(form) {
            textEditor { template.currentPassword }
            textEditor { template.newPassword }
            textEditor { template.confirmPassword }
        }

        button(Strings.save) .. alignSelf.end .. onClick {
            workspace.io {
                val service = getService<AuthPrincipalApi>(workspace.transport)

                service.addCredential(
                    principalId,
                    Credential(CredentialType.PASSWORD, form.inputValue.newPassword, now()),
                    Credential(CredentialType.PASSWORD, form.inputValue.currentPassword, now())
                )

                successNotification(Strings.saveSuccess)
            }

            hide()
        }
    }
}