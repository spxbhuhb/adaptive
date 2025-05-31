package `fun`.adaptive.auth.app

import `fun`.adaptive.auth.model.Session
import `fun`.adaptive.runtime.AbstractApplication
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.FrontendWorkspace
import `fun`.adaptive.utility.firstInstanceOrNull

open class AuthAppContext(
    val workspace: AbstractWorkspace
) {

    var sessionOrNull : Session? = null

    companion object {
        // FIXME authcontext lookup
        val AbstractApplication<*,*>.authContext
            get() = checkNotNull(frontendWorkspace.contexts.firstInstanceOrNull<AuthAppContext>()) { "cannot find auth context" }
    }
}