package `fun`.adaptive.app.ws.auth

import `fun`.adaptive.lib_app.generated.resources.accountSelf
import `fun`.adaptive.lib_app.generated.resources.accounts
import `fun`.adaptive.lib_app.generated.resources.military_tech
import `fun`.adaptive.lib_app.generated.resources.roles
import `fun`.adaptive.lib_app.generated.resources.signInTitle
import `fun`.adaptive.lib_app.generated.resources.supervised_user_circle
import `fun`.adaptive.app.ws.auth.account.wsAppAccountSelf
import `fun`.adaptive.app.ws.auth.account.wsAppAccountSelfDef
import `fun`.adaptive.app.ws.auth.admin.account.wsAppAccountManager
import `fun`.adaptive.app.ws.auth.admin.account.wsAppAccountManagerDef
import `fun`.adaptive.app.ws.auth.admin.role.wsAppRoleManager
import `fun`.adaptive.app.ws.auth.admin.role.wsAppRoleManagerDef
import `fun`.adaptive.app.ws.auth.signOut.wsAppSignOutActionDef
import `fun`.adaptive.app.ws.auth.signin.wsAppSignIn
import `fun`.adaptive.app.ws.auth.signin.wsAppSignInDef
import `fun`.adaptive.auth.app.AuthBasicClientModule
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.SingularWsItem

class AppAuthWsModule<WT : Workspace> : AuthBasicClientModule<WT>() {

    val SIGN_IN_KEY: FragmentKey = "app:ws:auth:sign-in"
    val SIGN_IN_ITEM by lazy { SingularWsItem(Strings.signInTitle, SIGN_IN_KEY) }

    val ACCOUNT_SELF_KEY: FragmentKey = "app:ws:auth:account:self"
    val ACCOUNT_SELF_ITEM by lazy { SingularWsItem(Strings.accountSelf, ACCOUNT_SELF_KEY) }

    val ACCOUNT_MANAGER_KEY: FragmentKey = "app:ws:admin:accounts"
    val ACCOUNT_MANAGER_ITEM by lazy { SingularWsItem(Strings.accounts, ACCOUNT_MANAGER_KEY, Graphics.supervised_user_circle) }

    val ROLE_MANAGER_KEY : FragmentKey = "app:ws:admin:roles"
    val ROLE_MANAGER_ITEM by lazy { SingularWsItem(Strings.roles, ROLE_MANAGER_KEY, Graphics.military_tech) }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(SIGN_IN_KEY, ::wsAppSignIn)
        add(ACCOUNT_SELF_KEY, ::wsAppAccountSelf)
        add(ACCOUNT_MANAGER_KEY, ::wsAppAccountManager)
        add(ROLE_MANAGER_KEY, ::wsAppRoleManager)
    }

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        wsAppSignInDef(application, this@AppAuthWsModule, SIGN_IN_ITEM)
        wsAppSignOutActionDef(application)
        wsAppAccountSelfDef(this@AppAuthWsModule)
        wsAppAccountManagerDef(this@AppAuthWsModule)
        wsAppRoleManagerDef(this@AppAuthWsModule)
    }

}