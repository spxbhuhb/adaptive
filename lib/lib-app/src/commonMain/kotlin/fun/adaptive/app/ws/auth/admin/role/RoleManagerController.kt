package `fun`.adaptive.app.ws.auth.admin.role

import `fun`.adaptive.app.ws.auth.AppAuthWsModule
import `fun`.adaptive.auth.api.AuthRoleApi
import `fun`.adaptive.auth.model.RoleSpec
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.generated.resources.saveFail
import `fun`.adaptive.ui.generated.resources.saveSuccess
import `fun`.adaptive.ui.workspace.logic.WsSingularPaneController
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.local.AvLocalItemList

class RoleManagerController(
    module: AppAuthWsModule<*>
) : WsSingularPaneController(module.workspace, module.ROLE_MANAGER_ITEM) {

    val service = getService<AuthRoleApi>(transport)

    val roles = AvLocalItemList<RoleSpec>(RoleSpec::class, service, workspace.backend)

    fun save(data: AvValue<RoleSpec>, add: Boolean) {
        remote(Strings.saveSuccess, Strings.saveFail) {
            service.save(
                if (add) null else data.uuid,
                data.name,
                data.spec
            )
        }
    }

}