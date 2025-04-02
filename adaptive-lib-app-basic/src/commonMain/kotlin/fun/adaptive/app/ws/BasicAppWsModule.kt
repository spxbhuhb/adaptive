package `fun`.adaptive.app.ws

import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.administration
import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.local_police
import `fun`.adaptive.app.ClientApplication
import `fun`.adaptive.app.ws.admin.WsAdminContext
import `fun`.adaptive.app.ws.auth.account.wsAppAccountSelfDef
import `fun`.adaptive.app.ws.auth.signOut.wsAppSignOutActionDef
import `fun`.adaptive.app.ws.main.backend.WsAppBackendFragmentFactory
import `fun`.adaptive.app.ws.main.frontend.WsAppFrontendFragmentFactory
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.builtin.eco
import `fun`.adaptive.ui.builtin.home
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.WsSideBarAction
import `fun`.adaptive.ui.workspace.logic.WsSingularPaneController
import `fun`.adaptive.ui.workspace.logic.WsUnitPaneController
import `fun`.adaptive.ui.workspace.model.SingularWsItem
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.ui.workspace.model.WsPaneSingularity
import `fun`.adaptive.utility.UUID

class BasicAppWsModule<WT : Workspace> : AppModule<WT>() {

    companion object {
        const val FRONTEND_MAIN_KEY: FragmentKey = "app:ws:frontend:main"
        const val BACKEND_MAIN_KEY: FragmentKey = "app:ws:backend:main"
        const val ADMIN_TOOL_KEY: FragmentKey = "app:ws:admin:tool"
        const val HOME_CONTENT_KEY: FragmentKey = "app:ws:home:content"

        val AdaptiveFragment.wsApplication
            get() = this.firstContext<ClientApplication<Workspace>>()

        fun AdaptiveFragment.wsAddContent(item: NamedItem, modifiers: Set<EventModifier> = emptySet()) {
            wsApplication.workspace.addContent(item, modifiers)
        }

    }

    lateinit var ACCOUNT_SELF_ITEM: SingularWsItem
    lateinit var HOME_CONTENT_ITEM: SingularWsItem

    override fun contextInit() {
        application.workspace.contexts += WsAdminContext()
    }

    override fun resourceInit() {
        application.stringStores += commonMainStringsStringStore0
    }

    override fun backendAdapterInit(adapter: BackendAdapter) = with(adapter) {
        + WsAppBackendFragmentFactory
    }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter) {
        + WsAppFrontendFragmentFactory
    }

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        wsAppHomePaneDef()
        appAdminToolDef()
        wsAppAccountSelfDef()
        wsAppSignOutActionDef(application)
    }

    fun Workspace.appAdminToolDef() {

        + WsPane(
            UUID(),
            this,
            Strings.administration,
            Graphics.local_police,
            WsPanePosition.RightMiddle,
            ADMIN_TOOL_KEY,
            Unit,
            WsUnitPaneController(this),
            displayOrder = Int.MAX_VALUE
        )

    }

    fun Workspace.wsAppHomePaneDef() {

        HOME_CONTENT_ITEM = SingularWsItem(Strings.home, HOME_CONTENT_KEY)

        addContentPaneBuilder(HOME_CONTENT_KEY) {
            WsPane(
                UUID(),
                workspace = this,
                Strings.home,
                Graphics.eco,
                WsPanePosition.Center,
                HOME_CONTENT_KEY,
                HOME_CONTENT_ITEM,
                WsSingularPaneController(this, HOME_CONTENT_ITEM),
                singularity = WsPaneSingularity.SINGULAR,
                displayOrder = 0
            )
        }

        + WsSideBarAction(
            workspace = this,
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



}