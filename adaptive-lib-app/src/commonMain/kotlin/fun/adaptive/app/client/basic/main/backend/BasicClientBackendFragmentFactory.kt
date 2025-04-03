package `fun`.adaptive.app.client.basic.main.backend

import `fun`.adaptive.app.client.basic.BasicAppClientModule.Companion.BASIC_CLIENT_BACKEND_MAIN_KEY
import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object BasicClientBackendFragmentFactory : FoundationFragmentFactory() {
    init {
        add(BASIC_CLIENT_BACKEND_MAIN_KEY, ::basicClientBackendMain)
    }
}