package `fun`.adaptive.app.ws

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.iot.app.commonMainStringsStringStore0
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.workspace.Workspace

class IotAppWsModule() : AppModule<Workspace>() {

    override suspend fun loadResources() {
        commonMainStringsStringStore0.load()
    }

    override fun AdaptiveAdapter.init() {
        fragmentFactory += AppFragmentFactory
    }

}