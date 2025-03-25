package `fun`.adaptive.app.ws

import `fun`.adaptive.foundation.AdaptiveFragmentFactory

object AppFragmentFactory : AdaptiveFragmentFactory() {
    init {
        add(WsAppModule.HOME_CONTENT_KEY, ::appHome)
    }
}