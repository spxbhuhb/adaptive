package `fun`.adaptive.app.ws

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.iot.app.commonMainStringsStringStore0
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.workspace.Workspace

class IotAppWsModule<WT : Workspace>() : AppModule<WT>() {

    override fun resourceInit() {
        application.stringStores += commonMainStringsStringStore0
    }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(application.wsAppMain.HOME_CONTENT_KEY, ::appHome)
    }

}