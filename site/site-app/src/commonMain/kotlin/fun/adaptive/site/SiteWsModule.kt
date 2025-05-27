package `fun`.adaptive.site

import `fun`.adaptive.app.ws.wsAppMain
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace

class SiteWsModule<WT : MultiPaneWorkspace>() : AppModule<WT>() {

    override fun resourceInit() {
        //application.stringStores += commonMainStringsStringStore0
    }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(application.wsAppMain.HOME_CONTENT_KEY, ::siteHome)
    }

}