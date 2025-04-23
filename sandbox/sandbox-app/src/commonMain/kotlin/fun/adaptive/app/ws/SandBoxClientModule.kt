package `fun`.adaptive.app.ws

import `fun`.adaptive.app.client.basic.BasicAppClientModule
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.runtime.ClientWorkspace
import `fun`.adaptive.sandbox.app.generated.resources.commonMainStringsStringStore0

open class SandBoxClientModule<WT : ClientWorkspace> : BasicAppClientModule<WT>() {

    override fun resourceInit() {
        application.stringStores += commonMainStringsStringStore0
    }

    override fun frontendAdapterInit(adapter : AdaptiveAdapter)= with(adapter.fragmentFactory) {
        add(BASIC_CLIENT_FRONTEND_MAIN_KEY, ::appHome)
    }

}