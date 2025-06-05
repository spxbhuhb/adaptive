package `fun`.adaptive.sandbox.app.mpw.app

import `fun`.adaptive.app.ui.mpw.mpwAppMainModule
import `fun`.adaptive.sandbox.app.mpw.ui.appHome
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace

class AppProjectHomeModule<FW : MultiPaneWorkspace, BW : AbstractWorkspace>() : AppModule<FW, BW>() {

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(application.mpwAppMainModule.HOME_CONTENT_KEY, ::appHome)
    }

}