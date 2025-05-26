package `fun`.adaptive.app.ws

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.lib_app.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.generated.resources.eco
import `fun`.adaptive.ui.generated.resources.home
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.ui.workspace.WsSideBarAction
import `fun`.adaptive.ui.workspace.logic.WsUnitPaneViewBackend
import `fun`.adaptive.ui.workspace.model.SingularWsItem
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.ui.workspace.model.WsPaneSingularity
import `fun`.adaptive.utility.UUID

class AppMainWsModule<WT : MultiPaneWorkspace> : AppModule<WT>() {

    val FRONTEND_MAIN_KEY: FragmentKey = "app:ws:frontend:main"
    val BACKEND_MAIN_KEY: FragmentKey = "app:ws:backend:main"

    val HOME_CONTENT_KEY: FragmentKey = "app:ws:home:content"
    val HOME_CONTENT_ITEM by lazy { SingularWsItem(Strings.home, HOME_CONTENT_KEY) }

    override fun resourceInit() {
        application.stringStores += commonMainStringsStringStore0
    }

    override fun backendAdapterInit(adapter: BackendAdapter) = with(adapter) {
        fragmentFactory.add(BACKEND_MAIN_KEY, ::wsAppBackendMain)

    }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(FRONTEND_MAIN_KEY, ::wsAppFrontendMain)
    }

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        wsAppHomePaneDef()
    }

    fun MultiPaneWorkspace.wsAppHomePaneDef() {

        addContentPaneBuilder(HOME_CONTENT_KEY) {
            WsPane(
                UUID(),
                workspace = this,
                Strings.home,
                Graphics.eco,
                WsPanePosition.Center,
                HOME_CONTENT_KEY,
                WsUnitPaneViewBackend(this),
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