package `fun`.adaptive.app.ws

import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.administration
import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.local_police
import `fun`.adaptive.app.ws.main.backend.WsAppBackendFragmentFactory
import `fun`.adaptive.app.ws.main.frontend.WsAppFrontendFragmentFactory
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.AppModule
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

object WsAppModule : AppModule<Workspace>() {

    const val FRONTEND_MAIN_KEY: FragmentKey = "app:ws:frontend:main"
    const val BACKEND_MAIN_KEY: FragmentKey = "app:ws:backend:main"

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
        wsAppAccountToolDef()
        wsAppSignOutActionDef()
    }

    const val ACCOUNT_TOOL_KEY: FragmentKey = "app:ws:account:tool"

    fun Workspace.wsAppAccountToolDef() {
        + WsPane(
            UUID(),
            Strings.account,
            Graphics.account_circle,
            WsPanePosition.LeftBottom,
            ACCOUNT_TOOL_KEY,
            Unit,
            WsUnitPaneController(),
            displayOrder = Int.MAX_VALUE - 1
        )
    }

    const val ADMIN_TOOL_KEY: FragmentKey = "app:ws:admin:tool"

    fun Workspace.appAdminToolDef() {

        + WsPane(
            UUID(),
            Strings.administration,
            Graphics.local_police,
            WsPanePosition.RightBottom,
            ADMIN_TOOL_KEY,
            Unit,
            WsUnitPaneController(),
            displayOrder = Int.MAX_VALUE - 1
        )

    }

    const val HOME_CONTENT_KEY: FragmentKey = "app:ws:home:content"

    lateinit var HOME_CONTENT_ITEM : SingularWsItem

    fun Workspace.wsAppHomePaneDef() {

        HOME_CONTENT_ITEM = SingularWsItem(Strings.home, HOME_CONTENT_KEY)

        + WsPane(
            UUID(),
            Strings.home,
            Graphics.eco,
            WsPanePosition.Center,
            HOME_CONTENT_KEY,
            HOME_CONTENT_ITEM,
            WsSingularPaneController(HOME_CONTENT_ITEM),
            singularity = WsPaneSingularity.SINGULAR,
            displayOrder = 0
        ).also { pane ->
            addContentPaneBuilder(HOME_CONTENT_KEY) { pane }
        }

    }

    fun Workspace.wsAppSignOutActionDef() {

        + WsSideBarAction(
            Strings.signOut,
            Graphics.power_settings_new,
            WsPanePosition.LeftBottom,
            Int.MAX_VALUE,
            null
        ) { }

    }

}