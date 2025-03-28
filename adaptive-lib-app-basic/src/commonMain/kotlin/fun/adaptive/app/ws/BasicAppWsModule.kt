package `fun`.adaptive.app.ws

import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.administration
import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.local_police
import `fun`.adaptive.app.UiClientApplication
import `fun`.adaptive.app.ws.main.backend.WsAppBackendFragmentFactory
import `fun`.adaptive.app.ws.main.frontend.WsAppFrontendFragmentFactory
import `fun`.adaptive.auth.api.AuthSessionApi
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.builtin.*
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.WsSideBarAction
import `fun`.adaptive.ui.workspace.logic.WsSingularPaneController
import `fun`.adaptive.ui.workspace.logic.WsUnitPaneController
import `fun`.adaptive.ui.workspace.model.SingularWsItem
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.ui.workspace.model.WsPaneSingularity
import `fun`.adaptive.utility.UUID

class BasicAppWsModule<AT :  UiClientApplication<Workspace, *>> : AppModule<Workspace, AT>() {

    companion object {
        const val FRONTEND_MAIN_KEY: FragmentKey = "app:ws:frontend:main"
        const val BACKEND_MAIN_KEY: FragmentKey = "app:ws:backend:main"
        const val ACCOUNT_SELF_KEY: FragmentKey = "app:ws:account:self"
        const val ADMIN_TOOL_KEY: FragmentKey = "app:ws:admin:tool"
        const val HOME_CONTENT_KEY: FragmentKey = "app:ws:home:content"

        val AdaptiveFragment.wsApplication
            get() = this.firstContext<UiClientApplication<Workspace, *>>()
    }

    lateinit var ACCOUNT_SELF_ITEM: SingularWsItem
    lateinit var HOME_CONTENT_ITEM: SingularWsItem

    override suspend fun loadResources() {
        commonMainStringsStringStore0.load()
    }

    override fun BackendAdapter.init() {
        fragmentFactory += WsAppBackendFragmentFactory
    }

    override fun AdaptiveAdapter.init() {
        fragmentFactory += WsAppFrontendFragmentFactory
    }

    override fun Workspace.init() {
        wsAppHomePaneDef()
        appAdminToolDef()
        wsAppAccountSelfDef()
        wsAppSignOutActionDef()
    }

    fun Workspace.wsAppAccountSelfDef() {

        ACCOUNT_SELF_ITEM = SingularWsItem(Strings.home, ACCOUNT_SELF_KEY)

        addContentPaneBuilder(ACCOUNT_SELF_KEY) {
            WsPane(
                UUID(),
                Strings.account,
                Graphics.account_circle,
                WsPanePosition.Center,
                ACCOUNT_SELF_KEY,
                ACCOUNT_SELF_ITEM,
                WsSingularPaneController(ACCOUNT_SELF_ITEM),
                displayOrder = Int.MAX_VALUE - 1
            )
        }

        + WsSideBarAction(
            Strings.account,
            Graphics.account_circle,
            WsPanePosition.LeftBottom,
            displayOrder = Int.MAX_VALUE - 1,
            null
        ) {
            addContent(ACCOUNT_SELF_ITEM)
        }
    }

    fun Workspace.appAdminToolDef() {

        + WsPane(
            UUID(),
            Strings.administration,
            Graphics.local_police,
            WsPanePosition.RightMiddle,
            ADMIN_TOOL_KEY,
            Unit,
            WsUnitPaneController(),
            displayOrder = Int.MAX_VALUE
        )

    }

    fun Workspace.wsAppHomePaneDef() {

        HOME_CONTENT_ITEM = SingularWsItem(Strings.home, HOME_CONTENT_KEY)

        addContentPaneBuilder(HOME_CONTENT_KEY) {
            WsPane(
                UUID(),
                Strings.home,
                Graphics.eco,
                WsPanePosition.Center,
                HOME_CONTENT_KEY,
                HOME_CONTENT_ITEM,
                WsSingularPaneController(HOME_CONTENT_ITEM),
                singularity = WsPaneSingularity.SINGULAR,
                displayOrder = 0
            )
        }

        + WsSideBarAction(
            Strings.home,
            Graphics.eco,
            WsPanePosition.LeftTop,
            0,
            null
        ) {
            addContent(HOME_CONTENT_ITEM)
        }

        if (this.lastActiveContentPaneGroup == null) {
            addContent(HOME_CONTENT_ITEM)
        }

    }

    fun Workspace.wsAppSignOutActionDef() {

        + WsSideBarAction(
            Strings.signOut,
            Graphics.power_settings_new,
            WsPanePosition.LeftBottom,
            Int.MAX_VALUE,
            null
        ) {
            io {
                getService<AuthSessionApi>(transport).logout()
                ui {
                    application.onSignOut()
                }
            }
        }

    }

}