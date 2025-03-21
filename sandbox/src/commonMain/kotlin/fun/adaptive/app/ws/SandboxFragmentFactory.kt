package `fun`.adaptive.app.ws

import `fun`.adaptive.foundation.AdaptiveFragmentFactory
import `fun`.adaptive.ui.codefence.codeFence
import `fun`.adaptive.ui.misc.todo
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.wsCenterPane
import `fun`.adaptive.ui.workspace.wsEmptyCenterPane
import `fun`.adaptive.ui.workspace.wsNoContent

object SandboxFragmentFactory : AdaptiveFragmentFactory() {
    init {
        add(WsAppModule.HOME_CONTENT_KEY, ::appHome)
    }
}