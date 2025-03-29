package `fun`.adaptive.app.client.basic.main.frontend

import `fun`.adaptive.app.client.basic.BasicAppClientModule.Companion.BASIC_CLIENT_FRONTEND_MAIN_KEY
import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object BasicClientFrontendFragmentFactory : FoundationFragmentFactory() {
    init {
        add(BASIC_CLIENT_FRONTEND_MAIN_KEY, ::basicClientFrontendMain)
    }
}