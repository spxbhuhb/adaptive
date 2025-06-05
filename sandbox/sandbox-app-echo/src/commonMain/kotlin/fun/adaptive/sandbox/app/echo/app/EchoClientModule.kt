package `fun`.adaptive.sandbox.app.echo.app

import `fun`.adaptive.app.app.AppMainModuleBasic
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.FrontendWorkspace
import `fun`.adaptive.sandbox.app.echo.ui.echoFrontendMain

class EchoClientModule<FW : FrontendWorkspace,BW: AbstractWorkspace> : AppMainModuleBasic<FW,BW>() {

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(BASIC_CLIENT_FRONTEND_MAIN_KEY, ::echoFrontendMain)
    }

}