package `fun`.adaptive.sandbox.app.mpw.app

import `fun`.adaptive.sandbox.app.mpw.ui.appHome
import `fun`.adaptive.app.ws.wsAppMain
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace

class AppProjectHomeModule<WT : MultiPaneWorkspace>() : AppModule<WT>() {

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(application.wsAppMain.HOME_CONTENT_KEY, ::appHome)
    }

}