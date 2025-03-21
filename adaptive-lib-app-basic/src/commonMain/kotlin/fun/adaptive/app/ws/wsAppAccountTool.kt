package `fun`.adaptive.app.ws

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.misc.todo
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.wsToolPane

@Adaptive
fun wsAppAccountTool(pane : WsPane<*,*>) : AdaptiveFragment {

    wsToolPane(pane) {
        todo()
    }

    return fragment()
}