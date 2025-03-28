package `fun`.adaptive.app.ws.main.backend

import `fun`.adaptive.app.ws.BasicAppWsModule
import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object WsAppBackendFragmentFactory : FoundationFragmentFactory() {
    init {
        with(BasicAppWsModule) {
            add(BACKEND_MAIN_KEY, ::wsAppBackendMain)
        }
    }
}