package `fun`.adaptive.iot.ui.space

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.ui.workspace.Workspace.Companion.wsContext
import `fun`.adaptive.ui.workspace.model.WsPane

@Adaptive
fun wsSpaceContentPane(pane : WsPane<*>): AdaptiveFragment {
    val context = fragment().wsContext<AioWsContext>()

    spaceEditor(context.spaceTree)

    return fragment()
}