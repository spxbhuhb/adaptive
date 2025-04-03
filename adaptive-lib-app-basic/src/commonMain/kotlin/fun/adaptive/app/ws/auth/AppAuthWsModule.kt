package `fun`.adaptive.app.ws.auth

import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.accountSelf
import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.signInTitle
import `fun`.adaptive.app.ws.auth.account.wsAppAccountSelf
import `fun`.adaptive.app.ws.auth.account.wsAppAccountSelfDef
import `fun`.adaptive.app.ws.auth.admin.account.ACCOUNT_MANAGER_KEY
import `fun`.adaptive.app.ws.auth.admin.account.wsAppAccountManager
import `fun`.adaptive.app.ws.auth.admin.account.wsAppAccountManagerDef
import `fun`.adaptive.app.ws.auth.admin.role.ROLE_MANAGER_KEY
import `fun`.adaptive.app.ws.auth.admin.role.wsAppRoleManager
import `fun`.adaptive.app.ws.auth.admin.role.wsAppRoleManagerDef
import `fun`.adaptive.app.ws.auth.signOut.wsAppSignOutActionDef
import `fun`.adaptive.app.ws.auth.signin.wsAppSignIn
import `fun`.adaptive.app.ws.auth.signin.wsAppSignInDef
import `fun`.adaptive.auth.app.AuthBasicClientModule
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.builtin.home
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.SingularWsItem

class AppAuthWsModule<WT : Workspace> : AuthBasicClientModule<WT>() {

    val SIGN_IN_KEY: FragmentKey
        get() = "app:ws:auth:sign-in"

    val SIGN_IN_ITEM by lazy { SingularWsItem(Strings.signInTitle, SIGN_IN_KEY) }

    val ACCOUNT_SELF_KEY : FragmentKey
        get() = "app:ws:auth:account:self"

    val ACCOUNT_SELF_ITEM by lazy { SingularWsItem(Strings.accountSelf, ACCOUNT_SELF_KEY) }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter) {
        fragmentFactory.add(SIGN_IN_KEY, ::wsAppSignIn)
        fragmentFactory.add(ACCOUNT_SELF_KEY, ::wsAppAccountSelf)
        fragmentFactory.add(ACCOUNT_MANAGER_KEY, ::wsAppAccountManager)
        fragmentFactory.add(ROLE_MANAGER_KEY, ::wsAppRoleManager)
    }

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        wsAppSignInDef(application, this@AppAuthWsModule, SIGN_IN_ITEM)
        wsAppSignOutActionDef(application)
        wsAppAccountSelfDef(this@AppAuthWsModule)
        wsAppAccountManagerDef()
        wsAppRoleManagerDef()
    }

}