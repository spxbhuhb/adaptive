package `fun`.adaptive.app.ui.mpw.auth

import `fun`.adaptive.app.ClientApplication
import `fun`.adaptive.app.app.AppMainModuleMpw
import `fun`.adaptive.app.ui.common.admin.account.accountManager
import `fun`.adaptive.app.ui.common.admin.role.roleManager
import `fun`.adaptive.app.ui.common.user.account.mpwAccountSelf
import `fun`.adaptive.app.ui.common.user.signin.signIn
import `fun`.adaptive.app.ui.mpw.addAdminPlugin
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

class AppAuthModuleMpw<WT : MultiPaneWorkspace, BW : BackendWorkspace> : AuthBasicClientModule<WT, BW>() {

    val SIGN_IN_KEY: FragmentKey = "app:mpw:auth:sign-in"
    val SIGN_IN_ITEM by lazy { SingularPaneItem(Strings.signInTitle, SIGN_IN_KEY) }

    val ACCOUNT_SELF_KEY: FragmentKey = "app:mpw:auth:account:self"
    val ACCOUNT_SELF_ITEM by lazy { SingularPaneItem(Strings.accountSelf, ACCOUNT_SELF_KEY) }

    val ACCOUNT_MANAGER_KEY: FragmentKey = "app:mpw:admin:accounts"
    val ACCOUNT_MANAGER_ITEM by lazy { SingularPaneItem(Strings.accounts, ACCOUNT_MANAGER_KEY, Graphics.supervised_user_circle) }

    val ROLE_MANAGER_KEY: FragmentKey = "app:mpw:admin:roles"
    val ROLE_MANAGER_ITEM by lazy { SingularPaneItem(Strings.roles, ROLE_MANAGER_KEY, Graphics.military_tech) }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(SIGN_IN_KEY, ::signIn)
        add(ACCOUNT_SELF_KEY, ::mpwAccountSelf)
        add(ACCOUNT_MANAGER_KEY, ::accountManager)
        add(ROLE_MANAGER_KEY, ::roleManager)
    }

    override fun frontendWorkspaceInit(workspace: WT, session: Any?) = with(workspace) {

        addSingularContentPane(ACCOUNT_SELF_ITEM) {
            UnitSingularContentViewBackend(
                workspace,
                PaneDef(
                    UUID(),
                    Strings.accountSelf,
                    Graphics.account_circle,
                    PanePosition.Center,
                    ACCOUNT_SELF_KEY,
                    displayOrder = Int.MAX_VALUE - 1
                ),
                ACCOUNT_SELF_ITEM
            )
        }

        addAdminPlugin(ACCOUNT_MANAGER_ITEM)
        addSingularContentPane(ACCOUNT_MANAGER_ITEM) {
            UnitSingularContentViewBackend(
                workspace,
                PaneDef(
                    UUID(),
                    Strings.accounts,
                    Graphics.supervised_user_circle,
                    PanePosition.Center,
                    ACCOUNT_MANAGER_KEY
                ),
                ACCOUNT_MANAGER_ITEM
            )
        }

        addAdminPlugin(ROLE_MANAGER_ITEM)
        addSingularContentPane(ROLE_MANAGER_ITEM) {
            UnitSingularContentViewBackend(
                workspace,
                PaneDef(
                    UUID(),
                    Strings.roles,
                    Graphics.military_tech,
                    PanePosition.Center,
                    ROLE_MANAGER_KEY
                ),
                ROLE_MANAGER_ITEM
            )
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
            val appModule = application.modules.firstInstance<AppMainModuleMpw<*, *>>()
            checkNotNull(application) { "AppWsModule not found" }
            addContent(appModule.HOME_CONTENT_ITEM)
        }

        + SideBarAction(
            Strings.accountSelf,
            Graphics.account_circle,
            PanePosition.LeftBottom,
            Int.MAX_VALUE - 1,
            null
        ) {
            addContent(ACCOUNT_SELF_ITEM)
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