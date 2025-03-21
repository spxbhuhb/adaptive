package `fun`.adaptive.app.ws.main.backend

import `fun`.adaptive.app.ws.WsAppModule
import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object WsAppBackendFragmentFactory : FoundationFragmentFactory() {
    init {
        with(WsAppModule) {
            add(BACKEND_MAIN_KEY, ::wsAppBackendMain)
        }
    }
}