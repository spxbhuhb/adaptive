package `fun`.adaptive.app.ws.auth

import `fun`.adaptive.app.ws.auth.BasicAppAuthWsModule.Companion.SIGN_IN_KEY
import `fun`.adaptive.app.ws.auth.admin.ACCOUNT_MANAGER_KEY
import `fun`.adaptive.app.ws.auth.admin.wsAppAccountManager
import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object WsAppAuthFragmentFactory : FoundationFragmentFactory() {
    init {
        add(SIGN_IN_KEY, ::wsAppSignIn)
        add(ACCOUNT_MANAGER_KEY, ::wsAppAccountManager)
    }
}