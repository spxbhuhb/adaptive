package `fun`.adaptive.app.server.main.backend

import `fun`.adaptive.app.server.BasicAppServerModule.Companion.SERVER_BACKEND_MAIN_KEY
import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object BasicAppServerFragmentFactory : FoundationFragmentFactory() {
    init {
        add(SERVER_BACKEND_MAIN_KEY, ::basicAppServerBackendMain)
    }
}