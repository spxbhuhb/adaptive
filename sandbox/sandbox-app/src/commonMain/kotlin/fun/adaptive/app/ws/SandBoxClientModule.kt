package `fun`.adaptive.app.ws

import `fun`.adaptive.app.app.AppMainModuleBasic
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.FrontendWorkspace
import `fun`.adaptive.sandbox.app.generated.resources.commonMainStringsStringStore0

open class SandBoxClientModule<FW : FrontendWorkspace,BW: AbstractWorkspace> : AppMainModuleBasic<FW,BW>() {

    override fun resourceInit() {
        application.stringStores += commonMainStringsStringStore0
    }

    override fun frontendAdapterInit(adapter : AdaptiveAdapter)= with(adapter.fragmentFactory) {
        add(BASIC_CLIENT_FRONTEND_MAIN_KEY, ::appHome)
    }

}