package `fun`.adaptive.app.ws.auth.admin.role

import `fun`.adaptive.app.ws.auth.AppAuthWsModule
import `fun`.adaptive.auth.api.AuthRoleApi
import `fun`.adaptive.auth.model.AuthMarkers
import `fun`.adaptive.auth.model.RoleSpec
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.generated.resources.saveFail
import `fun`.adaptive.ui.generated.resources.saveSuccess
import `fun`.adaptive.ui.mpw.backends.SingularPaneViewBackend
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.avByMarker
import `fun`.adaptive.value.local.AvListSubscriber

class RoleManagerViewBackend(
    module: AppAuthWsModule<*>
) : SingularPaneViewBackend<RoleManagerViewBackend>(module.workspace, module.ROLE_MANAGER_ITEM) {

    val service = getService<AuthRoleApi>(transport)

    val roles = AvListSubscriber(workspace.backend, RoleSpec::class, avByMarker(AuthMarkers.ROLE))

    fun save(data: AvValue<RoleSpec>, add: Boolean) {
        remote(Strings.saveSuccess, Strings.saveFail) {
            service.save(
                if (add) null else data.uuid,
                data.nameLike,
                data.spec
            )
        }
    }

}