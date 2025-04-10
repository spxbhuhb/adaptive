package `fun`.adaptive.app.ws

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.sandbox.commonMainStringsStringStore0
import `fun`.adaptive.ui.workspace.Workspace

object WsSandBoxModule : AppModule<Workspace>() {

    override fun resourceInit() {
        application.stringStores += commonMainStringsStringStore0
    }

    override fun frontendAdapterInit(adapter : AdaptiveAdapter)= with(adapter) {
        + SandboxFragmentFactory
    }

}