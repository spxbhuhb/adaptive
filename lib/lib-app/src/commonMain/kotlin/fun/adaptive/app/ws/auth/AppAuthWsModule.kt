package `fun`.adaptive.app.ws.auth

import `fun`.adaptive.app.ClientApplication
import `fun`.adaptive.app.ws.AppMainWsModule
import `fun`.adaptive.app.ws.addAdminItem
import `fun`.adaptive.app.ws.auth.account.AccountSelfViewBackend
import `fun`.adaptive.app.ws.auth.account.wsAppAccountSelf
import `fun`.adaptive.app.ws.auth.admin.account.AccountManagerViewBackend
import `fun`.adaptive.app.ws.auth.admin.account.wsAppAccountManager
import `fun`.adaptive.app.ws.auth.admin.role.RoleManagerViewBackend
import `fun`.adaptive.app.ws.auth.admin.role.wsAppRoleManager
import `fun`.adaptive.app.ws.auth.signin.wsAppSignIn
import `fun`.adaptive.auth.api.AuthSessionApi
import `fun`.adaptive.auth.app.AuthAppContext.Companion.authContext
import `fun`.adaptive.auth.app.AuthBasicClientModule
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.lib_app.generated.resources.*
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.generated.resources.account_circle
import `fun`.adaptive.ui.generated.resources.power_settings_new
import `fun`.adaptive.ui.generated.resources.signOut
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.SideBarAction
import `fun`.adaptive.ui.mpw.backends.UnitSingularContentViewBackend
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.ui.mpw.model.PaneSingularity
import `fun`.adaptive.ui.mpw.model.SingularPaneItem
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.firstInstance

class AppAuthWsModule<WT : MultiPaneWorkspace, BW : BackendWorkspace> : AuthBasicClientModule<WT, BW>() {

    val SIGN_IN_KEY: FragmentKey = "app:ws:auth:sign-in"
    val SIGN_IN_ITEM by lazy { SingularPaneItem(Strings.signInTitle, SIGN_IN_KEY) }

    val ACCOUNT_SELF_KEY: FragmentKey = "app:ws:auth:account:self"
    val ACCOUNT_SELF_ITEM by lazy { SingularPaneItem(Strings.accountSelf, ACCOUNT_SELF_KEY) }

    val ACCOUNT_MANAGER_KEY: FragmentKey = "app:ws:admin:accounts"
    val ACCOUNT_MANAGER_ITEM by lazy { SingularPaneItem(Strings.accounts, ACCOUNT_MANAGER_KEY, Graphics.supervised_user_circle) }

    val ROLE_MANAGER_KEY: FragmentKey = "app:ws:admin:roles"
    val ROLE_MANAGER_ITEM by lazy { SingularPaneItem(Strings.roles, ROLE_MANAGER_KEY, Graphics.military_tech) }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(SIGN_IN_KEY, ::wsAppSignIn)
        add(ACCOUNT_SELF_KEY, ::wsAppAccountSelf)
        add(ACCOUNT_MANAGER_KEY, ::wsAppAccountManager)
        add(ROLE_MANAGER_KEY, ::wsAppRoleManager)
    }

    override fun frontendWorkspaceInit(workspace: WT, session: Any?) = with(workspace) {

        addSingularContentPane(ACCOUNT_SELF_ITEM) {
            AccountSelfViewBackend(this@AppAuthWsModule)
        }

        addAdminItem(ACCOUNT_MANAGER_ITEM)
        addSingularContentPane(ACCOUNT_MANAGER_ITEM) {
            AccountManagerViewBackend(this@AppAuthWsModule)
        }

        addAdminItem(ROLE_MANAGER_ITEM)
        addSingularContentPane(ROLE_MANAGER_ITEM) {
            RoleManagerViewBackend(this@AppAuthWsModule)
        }

        addSingularContentPane(SIGN_IN_ITEM) {
            UnitSingularContentViewBackend(
                workspace,
                PaneDef(
                    UUID(),
                    Strings.signInTitle,
                    Graphics.account_circle,
                    PanePosition.Center,
                    SIGN_IN_KEY,
                    singularity = PaneSingularity.FULLSCREEN
                ),
                SIGN_IN_ITEM
            )
        }

        if (application.authContext.sessionOrNull == null) {
            addContent(SIGN_IN_ITEM)
        } else {
            val appModule = application.modules.firstInstance<AppMainWsModule<*,*>>()
            checkNotNull(application) { "AppWsModule not found" }
            addContent(appModule.HOME_CONTENT_ITEM)
        }

        + SideBarAction(
            Strings.signOut,
            Graphics.power_settings_new,
            PanePosition.LeftBottom,
            Int.MAX_VALUE,
            null
        ) {
            io {
                getService<AuthSessionApi>(transport).signOut()
                ui {
                    (application as? ClientApplication)?.onSignOut()
                }
            }
        }
    }
}