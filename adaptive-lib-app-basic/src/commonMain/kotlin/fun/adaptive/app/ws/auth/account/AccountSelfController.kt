package `fun`.adaptive.app.ws.auth.account

import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.saveSuccess
import `fun`.adaptive.auth.api.AuthPrincipalApi
import `fun`.adaptive.auth.api.basic.AuthBasicApi
import `fun`.adaptive.auth.app.AuthAppContext
import `fun`.adaptive.auth.model.basic.BasicAccountSpec
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.snackbar.successNotification
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsSingularPaneController
import `fun`.adaptive.utility.firstInstance
import `fun`.adaptive.value.AvValueId

class AccountSelfController(
    workspace : Workspace
) : WsSingularPaneController(workspace, ACCOUNT_SELF_ITEM) {

    suspend fun getAccountEditorData(principalId : AvValueId? = null): AccountEditorData? {

        val actualPrincipalId =
            principalId
                ?: workspace.contexts.firstInstance<AuthAppContext>().sessionOrNull?.principalOrNull
                ?: return null

        val principal = getService<AuthPrincipalApi>(transport).getOrNull(actualPrincipalId) ?: return null
        val account = getService<AuthBasicApi>(transport).account() ?: return null

        return AccountEditorData(
            principal.uuid,
            login = principal.name,
            name = account.name,
            email = account.email,
            activated = principal.spec.activated,
            locked = principal.spec.locked
        )
    }

    fun save(data : AccountEditorData) {
        io {
            getService<AuthBasicApi>(transport).save(data.uuid, data.name, BasicAccountSpec(data.email))
            ui {
                successNotification(Strings.saveSuccess)
            }
        }
    }
}