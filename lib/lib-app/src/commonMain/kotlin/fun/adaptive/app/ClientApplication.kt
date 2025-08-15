package `fun`.adaptive.app

import `fun`.adaptive.auth.model.AuthRole
import `fun`.adaptive.auth.model.Session
import `fun`.adaptive.foundation.value.observableOf
import `fun`.adaptive.runtime.AbstractClientApplication
import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.runtime.FrontendWorkspace
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.navigation.NavState
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.Url

abstract class ClientApplication<WT : FrontendWorkspace, BW : BackendWorkspace> : AbstractClientApplication<WT, BW>() {

    open val defaultFontName = "Open Sans"
    open val defaultFontSize = 16.sp
    open val defaultFontWeight = 300

    /**
     * All roles known by the application.
     *
     * **NOT** the roles of the current user.
     */
    var allApplicationRoles : List<AuthRole> = emptyList()

    open val navState = observableOf { NavState(Url.parse("/")) }

    open fun onSignIn() {

    }

    open fun onSignOut() {

    }

    override fun hasRole(roleName : String) : Boolean =
        allApplicationRoles.firstOrNull { it.name == roleName }?.let { role ->
            val session = genericSessionOrNull as? Session
            session?.roles?.contains(role.uuid) ?: false
        } ?: false

    override fun hasRole(roleUuid : UUID<*>) : Boolean =
        (genericSessionOrNull as? Session)?.roles?.contains(roleUuid) ?: false
}