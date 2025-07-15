package `fun`.adaptive.site.app

import `fun`.adaptive.app.ui.mpw.mpwAppMainModule
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.site.searchDialog
import `fun`.adaptive.site.siteHome
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.popup.modal.dialog

class SiteModuleMpw<FW : MultiPaneWorkspace, BW : AbstractWorkspace>() : AppModule<FW, BW>() {

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(application.mpwAppMainModule.HOME_CONTENT_KEY, ::siteHome)
    }


    override fun frontendWorkspaceInit(workspace: FW, session: Any?) {
        workspace.doubleShiftHandler = {
            dialog(workspace, workspace, ::searchDialog)
        }
    }
}