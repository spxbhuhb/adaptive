package `fun`.adaptive.app.ws.auth.account

import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.saveSuccess
import `fun`.adaptive.auth.api.AuthPrincipalApi
import `fun`.adaptive.auth.api.basic.AuthBasicApi
import `fun`.adaptive.auth.app.AuthAppContext
import `fun`.adaptive.auth.model.Credential
import `fun`.adaptive.auth.model.CredentialType
import `fun`.adaptive.auth.model.PrincipalSpec
import `fun`.adaptive.auth.model.basic.BasicAccountSpec
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.snackbar.successNotification
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsSingularPaneController
import `fun`.adaptive.utility.firstInstance
import `fun`.adaptive.value.AvValueId
import kotlinx.datetime.Clock.System.now

class AccountSelfController(
    workspace : Workspace
) : WsSingularPaneController(workspace, ACCOUNT_SELF_ITEM) {

    val service = getService<AuthBasicApi>(transport)

    suspend fun getAccountEditorData(principalId : AvValueId? = null): AccountEditorData? {

        val actualPrincipalId =
            principalId
                ?: workspace.contexts.firstInstance<AuthAppContext>().sessionOrNull?.principalOrNull
                ?: return null

        val principal = getService<AuthPrincipalApi>(transport).getOrNull(actualPrincipalId) ?: return null
        val account = service.account() ?: return null

        return AccountEditorData(
            principal.uuid,
            account.accountId,
            principalName = principal.name,
            name = account.name,
            email = account.email,
            activated = principal.spec.activated,
            locked = principal.spec.locked
        )
    }

    fun save(data : AccountEditorData) {
        io {
            service.save(
                principalId = data.principalId,
                data.principalName,
                PrincipalSpec(activated = data.activated, locked = data.locked, roles = data.roles),
                if (data.password.isEmpty()) null else Credential(CredentialType.PASSWORD, data.password, now()),
                data.name,
                BasicAccountSpec(data.email),
            )

            ui {
                successNotification(Strings.saveSuccess)
            }
        }
    }
}