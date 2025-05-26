package `fun`.adaptive.app.ws.auth.admin.account

import `fun`.adaptive.app.ws.auth.AppAuthWsModule
import `fun`.adaptive.app.ws.auth.account.AccountEditorData
import `fun`.adaptive.auth.api.basic.AuthBasicApi
import `fun`.adaptive.auth.model.Credential
import `fun`.adaptive.auth.model.CredentialType
import `fun`.adaptive.auth.model.PrincipalSpec
import `fun`.adaptive.auth.model.basic.BasicAccountSpec
import `fun`.adaptive.auth.util.BasicAccountSummaryStore
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.generated.resources.saveFail
import `fun`.adaptive.ui.generated.resources.saveSuccess
import `fun`.adaptive.ui.workspace.logic.WsSingularPaneViewBackend
import kotlinx.datetime.Clock.System.now

class AccountManagerViewBackend(
    module: AppAuthWsModule<*>
) : WsSingularPaneViewBackend<AccountManagerViewBackend>(module.workspace, module.ACCOUNT_MANAGER_ITEM) {

    val authBasic = getService<AuthBasicApi>(transport)

    val accounts = BasicAccountSummaryStore(workspace.backend)

    fun save(data: AccountEditorData) {
        remote(Strings.saveSuccess, Strings.saveFail) {
            authBasic.save(
                principalId = data.principalId,
                data.principalName,
                PrincipalSpec(activated = data.activated, locked = data.locked, roles = data.roles),
                if (data.principalId != null && data.password.isEmpty()) {
                    null
                } else {
                    Credential(CredentialType.PASSWORD, data.password, now())
                },
                data.name,
                BasicAccountSpec(data.email),
            )
        }
    }

}