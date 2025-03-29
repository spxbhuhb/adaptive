package `fun`.adaptive.auth.app

import `fun`.adaptive.auth.model.Session
import `fun`.adaptive.runtime.AbstractApplication
import `fun`.adaptive.utility.firstInstance

open class AuthAppContext {

    var sessionOrNull : Session? = null

    fun onSignOut() {

    }

    companion object {
        val AbstractApplication<*>.authContext
            get() = workspace.contexts.firstInstance<AuthAppContext>()
    }
}