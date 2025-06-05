package `fun`.adaptive.app.app

import `fun`.adaptive.app.client.basic.basicBackendMain
import `fun`.adaptive.app.ui.basic.basicFrontendMain
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.FrontendWorkspace

open class AppMainModuleBasic<FW : FrontendWorkspace, BW : AbstractWorkspace> : AppMainModule<FW, BW>() {

    companion object {
        const val BASIC_CLIENT_FRONTEND_MAIN_KEY: FragmentKey = "app:basic:client:frontend:main"
        const val BASIC_CLIENT_BACKEND_MAIN_KEY: FragmentKey = "app:basic:client:backend:main"
    }

    override fun backendAdapterInit(adapter: BackendAdapter) = with(adapter.fragmentFactory) {
        add(BASIC_CLIENT_BACKEND_MAIN_KEY, ::basicBackendMain)
    }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(BASIC_CLIENT_FRONTEND_MAIN_KEY, ::basicFrontendMain)
    }

}