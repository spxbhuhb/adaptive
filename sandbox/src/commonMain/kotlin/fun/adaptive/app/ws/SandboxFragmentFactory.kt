package `fun`.adaptive.app.ws

import `fun`.adaptive.foundation.AdaptiveFragmentFactory

object SandboxFragmentFactory : AdaptiveFragmentFactory() {
    init {
        add(BasicAppWsModule.HOME_CONTENT_KEY, ::appHome)
    }
}