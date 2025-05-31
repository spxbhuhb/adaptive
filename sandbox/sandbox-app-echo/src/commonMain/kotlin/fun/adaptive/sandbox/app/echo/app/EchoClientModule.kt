package `fun`.adaptive.sandbox.app.echo.app

import `fun`.adaptive.app.client.basic.BasicAppClientModule
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.runtime.FrontendWorkspace
import `fun`.adaptive.sandbox.app.echo.ui.echoFrontendMain

class EchoClientModule<WT : FrontendWorkspace> : BasicAppClientModule<WT>() {

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(BASIC_CLIENT_FRONTEND_MAIN_KEY, ::echoFrontendMain)
    }

}