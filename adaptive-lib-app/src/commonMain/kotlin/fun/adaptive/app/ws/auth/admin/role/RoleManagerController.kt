package `fun`.adaptive.app.ws.auth.admin.role

import `fun`.adaptive.adaptive_lib_app.generated.resources.saveFail
import `fun`.adaptive.adaptive_lib_app.generated.resources.saveSuccess
import `fun`.adaptive.auth.api.AuthRoleApi
import `fun`.adaptive.auth.model.RoleSpec
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsSingularPaneController
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.local.AvLocalItemList

class RoleManagerController(
    workspace: Workspace
) : WsSingularPaneController(workspace, ROLE_MANAGER_ITEM) {

    override val adminTool: Boolean
        get() = true

    val service = getService<AuthRoleApi>(transport)

    val roles = AvLocalItemList<RoleSpec>(workspace.backend, service, RoleSpec::class)

    fun save(data: AvItem<RoleSpec>, add: Boolean) {
        remote(Strings.saveSuccess, Strings.saveFail) {
            service.save(
                if (add) null else data.uuid,
                data.name,
                data.spec
            )
        }
    }

}