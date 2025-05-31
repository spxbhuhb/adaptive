package `fun`.adaptive.app.server

import `fun`.adaptive.app.server.main.backend.BasicAppServerFragmentFactory
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.BackendWorkspace

class BasicAppServerModule<FW : AbstractWorkspace, BW : BackendWorkspace> : AppModule<FW, BW>() {

    companion object {
        const val SERVER_BACKEND_MAIN_KEY: FragmentKey = "app:server:backend:main"
    }

    override fun backendAdapterInit(adapter: BackendAdapter) = with(adapter) {
        + BasicAppServerFragmentFactory
    }

}