package `fun`.adaptive.app.app

import `fun`.adaptive.app.server.serverBackendMain
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.BackendWorkspace

class AppMainModuleServer<FW : AbstractWorkspace, BW : BackendWorkspace> : AppMainModule<FW, BW>() {

    companion object {
        const val SERVER_BACKEND_MAIN_KEY: FragmentKey = "app:server:backend:main"
    }

    override fun backendAdapterInit(adapter: BackendAdapter) = with(adapter.fragmentFactory) {
        add(SERVER_BACKEND_MAIN_KEY, ::serverBackendMain)
    }

}