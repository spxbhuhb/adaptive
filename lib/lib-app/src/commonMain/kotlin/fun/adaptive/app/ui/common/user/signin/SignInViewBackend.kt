package `fun`.adaptive.app.ui.common.user.signin

import `fun`.adaptive.app.ClientApplication
import `fun`.adaptive.auth.api.AuthSessionApi
import `fun`.adaptive.auth.app.AuthAppContext.Companion.authContext
import `fun`.adaptive.auth.model.basic.BasicSignIn
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.lib_app.generated.resources.singInFail
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.FrontendWorkspace
import `fun`.adaptive.service.api.getService

class SignInViewBackend(fragment: AdaptiveFragment) {

    val workspace : FrontendWorkspace = fragment.firstContext<FrontendWorkspace>()

    fun signIn(
        actualSignIn: BasicSignIn,
        feedBack : (String) -> Unit
    ) {
        workspace.io {
            try {
                val app = workspace.application as ClientApplication<*, *>
                app.authContext.sessionOrNull = getService<AuthSessionApi>(workspace.transport).signIn(actualSignIn.accountName, actualSignIn.password)
                app.onSignIn()
            } catch (t: Throwable) {
                t.printStackTrace()
                feedBack(Strings.singInFail)
            }
        }
    }

}