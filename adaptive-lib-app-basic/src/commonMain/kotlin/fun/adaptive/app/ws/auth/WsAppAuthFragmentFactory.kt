package `fun`.adaptive.app.ws.auth

import `fun`.adaptive.app.ws.auth.BasicAppAuthWsModule.Companion.SIGN_IN_KEY
import `fun`.adaptive.app.ws.auth.admin.account.ACCOUNT_MANAGER_KEY
import `fun`.adaptive.app.ws.auth.admin.account.wsAppAccountManager
import `fun`.adaptive.app.ws.auth.admin.role.ROLE_MANAGER_KEY
import `fun`.adaptive.app.ws.auth.admin.role.wsAppRoleManager
import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object WsAppAuthFragmentFactory : FoundationFragmentFactory() {
    init {
        add(SIGN_IN_KEY, ::wsAppSignIn)
        add(ACCOUNT_MANAGER_KEY, ::wsAppAccountManager)
        add(ROLE_MANAGER_KEY, ::wsAppRoleManager)
    }
}