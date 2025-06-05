package `fun`.adaptive.app.ui.common.admin.role

import `fun`.adaptive.auth.api.AuthRoleApi
import `fun`.adaptive.auth.model.AuthMarkers
import `fun`.adaptive.auth.model.RoleSpec
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.FrontendWorkspace
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.generated.resources.saveFail
import `fun`.adaptive.ui.generated.resources.saveSuccess
import `fun`.adaptive.ui.snackbar.failNotification
import `fun`.adaptive.ui.snackbar.successNotification
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.avByMarker
import `fun`.adaptive.value.client.AvListSubscriber

class RoleManagerViewBackend(fragment: AdaptiveFragment) {

    val workspace = fragment.firstContext<FrontendWorkspace>()

    val service = getService<AuthRoleApi>(workspace.transport)

    val roles = AvListSubscriber(workspace.backend, RoleSpec::class, avByMarker(AuthMarkers.ROLE))

    fun save(data: AvValue<RoleSpec>, add: Boolean) {
        workspace.io {
            try {
                service.save(
                    if (add) null else data.uuid,
                    data.nameLike,
                    data.spec
                )
                successNotification(Strings.saveSuccess)
            } catch (ex: Exception) {
                workspace.logger.error(ex)
                failNotification(Strings.saveFail)
            }
        }
    }

}