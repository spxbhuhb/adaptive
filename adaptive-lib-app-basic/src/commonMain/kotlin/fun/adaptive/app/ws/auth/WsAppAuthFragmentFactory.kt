package `fun`.adaptive.app.ws.auth

import `fun`.adaptive.app.ws.auth.BasicAppAuthWsModule.Companion.SIGN_IN_KEY
import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object WsAppAuthFragmentFactory : FoundationFragmentFactory() {
    init {
        add(SIGN_IN_KEY, ::wsAppSignIn)
    }
}