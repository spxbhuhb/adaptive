package `fun`.adaptive.app.client.basic

import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.app.client.basic.main.backend.BasicClientBackendFragmentFactory
import `fun`.adaptive.app.client.basic.main.frontend.BasicClientFrontendFragmentFactory
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.ClientWorkspace

class BasicAppClientModule<WT : ClientWorkspace> : AppModule<WT>() {

    companion object {
        const val BASIC_CLIENT_FRONTEND_MAIN_KEY: FragmentKey = "app:basic:client:frontend:main"
        const val BASIC_CLIENT_BACKEND_MAIN_KEY: FragmentKey = "app:basic:client:backend:main"
    }

    override fun resourceInit() {
        application.stringStores += commonMainStringsStringStore0
    }

    override fun backendAdapterInit(adapter: BackendAdapter) = with(adapter) {
        + BasicClientBackendFragmentFactory
    }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter) {
        + BasicClientFrontendFragmentFactory
    }

}