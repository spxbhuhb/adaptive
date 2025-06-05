package `fun`.adaptive.app.app

import `fun`.adaptive.app.client.mpw.mpwBackendMain
import `fun`.adaptive.app.ui.mpw.mpwFrontendMain
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.ui.generated.resources.eco
import `fun`.adaptive.ui.generated.resources.home
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.SideBarAction
import `fun`.adaptive.ui.mpw.backends.UnitPaneViewBackend
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.ui.mpw.model.PaneSingularity
import `fun`.adaptive.ui.mpw.model.SingularPaneItem
import `fun`.adaptive.utility.UUID

class AppMainModuleMpw<FW : MultiPaneWorkspace, BW: BackendWorkspace> : AppMainModule<FW, BW>() {

    val FRONTEND_MAIN_KEY: FragmentKey = "app:mpw:frontend:main"
    val BACKEND_MAIN_KEY: FragmentKey = "app:mpw:backend:main"

    val HOME_CONTENT_KEY: FragmentKey = "app:mpw:home:content"
    val HOME_CONTENT_ITEM by lazy { SingularPaneItem(Strings.home, HOME_CONTENT_KEY) }

    override fun backendAdapterInit(adapter: BackendAdapter) = with(adapter) {
        fragmentFactory.add(BACKEND_MAIN_KEY, ::mpwBackendMain)

    }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(FRONTEND_MAIN_KEY, ::mpwFrontendMain)
    }

    override fun frontendWorkspaceInit(workspace: FW, session: Any?) = with(workspace) {
        homePaneDef()
    }

    fun MultiPaneWorkspace.homePaneDef() {

        val homePaneDef = PaneDef(
            UUID(),
            Strings.home,
            Graphics.eco,
            PanePosition.Center,
            HOME_CONTENT_KEY,
            singularity = PaneSingularity.SINGULAR,
            displayOrder = 0
        )

        addContentPaneBuilder(
            HOME_CONTENT_KEY,
            { true },
            { UnitPaneViewBackend(this, homePaneDef) }
        )

        + SideBarAction(
            Strings.home,
            Graphics.eco,
            PanePosition.LeftTop,
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