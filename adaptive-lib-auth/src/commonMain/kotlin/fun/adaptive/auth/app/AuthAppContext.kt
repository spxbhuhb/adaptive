package `fun`.adaptive.auth.app

import `fun`.adaptive.auth.model.Session
import `fun`.adaptive.runtime.AbstractApplication
import `fun`.adaptive.runtime.ClientWorkspace
import `fun`.adaptive.utility.firstInstanceOrNull

open class AuthAppContext(
    val workspace: ClientWorkspace
) {

    var sessionOrNull : Session? = null

    fun onSignOut() {

    }

    companion object {
        val AbstractApplication<*>.authContext
            get() = checkNotNull(workspace.contexts.firstInstanceOrNull<AuthAppContext>()) { "cannot find auth context" }
    }
}