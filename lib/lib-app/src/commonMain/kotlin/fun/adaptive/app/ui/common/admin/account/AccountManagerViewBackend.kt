package `fun`.adaptive.app.ui.common.admin.account

import `fun`.adaptive.app.ui.common.user.account.AccountEditorData
import `fun`.adaptive.auth.api.basic.AuthBasicApi
import `fun`.adaptive.auth.model.Credential
import `fun`.adaptive.auth.model.CredentialType
import `fun`.adaptive.auth.model.PrincipalSpec
import `fun`.adaptive.auth.model.basic.BasicAccountSpec
import `fun`.adaptive.auth.util.BasicAccountSummaryStore
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.FrontendWorkspace
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.generated.resources.saveFail
import `fun`.adaptive.ui.generated.resources.saveSuccess
import `fun`.adaptive.ui.snackbar.failNotification
import `fun`.adaptive.ui.snackbar.successNotification
import kotlinx.datetime.Clock.System.now

class AccountManagerViewBackend(
    fragment: AdaptiveFragment
) {

    val workspace = fragment.firstContext<FrontendWorkspace>()

    val authBasic = getService<AuthBasicApi>(workspace.transport)

    val accounts = BasicAccountSummaryStore(workspace.backend)

    fun save(data: AccountEditorData) {
        workspace.io {
            try {
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
                successNotification(Strings.saveSuccess)
            } catch (ex: Exception) {
                workspace.logger.error(ex)
                failNotification(Strings.saveFail)
            }
        }
    }

}