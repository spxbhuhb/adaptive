package `fun`.adaptive.app.ui.common.user.account

import `fun`.adaptive.auth.api.AuthPrincipalApi
import `fun`.adaptive.auth.api.basic.AuthBasicApi
import `fun`.adaptive.auth.app.AuthAppContext
import `fun`.adaptive.auth.model.Credential
import `fun`.adaptive.auth.model.CredentialType
import `fun`.adaptive.auth.model.PrincipalSpec
import `fun`.adaptive.auth.model.basic.BasicAccountSpec
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.FrontendWorkspace
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.generated.resources.saveSuccess
import `fun`.adaptive.ui.snackbar.successNotification
import `fun`.adaptive.utility.firstInstance
import `fun`.adaptive.value.AvValueId
import kotlin.time.Clock.System.now

class AccountSelfViewBackend(fragment: AdaptiveFragment) {

    val workspace : FrontendWorkspace = fragment.firstContext<FrontendWorkspace>()

    suspend fun getAccountEditorData(principalId: AvValueId? = null): AccountEditorData? {

        val actualPrincipalId =
            principalId
                ?: workspace.contexts.firstInstance<AuthAppContext>().sessionOrNull?.principalOrNull
                ?: return null

        val principal = getService<AuthPrincipalApi>(workspace.transport).getOrNull(actualPrincipalId) ?: return null
        val account = getService<AuthBasicApi>(workspace.transport).account() ?: return null

        return AccountEditorData(
            principal.uuid,
            account.accountId,
            principalName = principal.name ?: "",
            name = account.name,
            email = account.email,
            activated = principal.spec.activated,
            locked = principal.spec.locked,
            roles = principal.spec.roles
        )
    }

    fun save(data: AccountEditorData) {
        with (workspace) {
            io {
                getService<AuthBasicApi>(transport).save(
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
}