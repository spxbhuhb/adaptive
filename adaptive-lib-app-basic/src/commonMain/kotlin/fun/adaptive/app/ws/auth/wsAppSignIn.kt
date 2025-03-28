package `fun`.adaptive.app.ws.auth

import `fun`.adaptive.app.ws.auth.signin.largeSignIn
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.workspace.model.WsPane

@Adaptive
fun wsAppSignIn(@Suppress("unused") pane: WsPane<*, *>): AdaptiveFragment {
    largeSignIn()
    return fragment()
}