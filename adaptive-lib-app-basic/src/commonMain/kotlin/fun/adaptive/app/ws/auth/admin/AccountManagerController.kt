package `fun`.adaptive.app.ws.auth.admin

import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.saveSuccess
import `fun`.adaptive.app.ws.auth.account.AccountEditorData
import `fun`.adaptive.auth.api.basic.AuthBasicApi
import `fun`.adaptive.auth.model.Credential
import `fun`.adaptive.auth.model.CredentialType
import `fun`.adaptive.auth.model.PrincipalSpec
import `fun`.adaptive.auth.model.basic.BasicAccountSpec
import `fun`.adaptive.auth.util.BasicAccountSummaryStore
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsSingularPaneController
import kotlinx.datetime.Clock.System.now

class AccountManagerController(
    workspace: Workspace
) : WsSingularPaneController(workspace, ACCOUNT_MANAGER_ITEM) {

    override val adminTool: Boolean
        get() = true

    val authBasic = getService<AuthBasicApi>(transport)

    val accounts = BasicAccountSummaryStore(workspace.backend)

    fun add(data: AccountEditorData) {
        remote(Strings.saveSuccess, "ops") {
            authBasic.add(
                data.principalName,
                PrincipalSpec(activated = true),
                Credential(CredentialType.PASSWORD, data.password, now()),
                data.roles,
                data.name,
                BasicAccountSpec(data.email),
            )
        }
    }

    fun save(data: AccountEditorData) {
        remote(Strings.saveSuccess, "ops") {
            authBasic.save(
                data.principalId!!,
                data.name,
                BasicAccountSpec(data.email)
            )
        }
    }

}