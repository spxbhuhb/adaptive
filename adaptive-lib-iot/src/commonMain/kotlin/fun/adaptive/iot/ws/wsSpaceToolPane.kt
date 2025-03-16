package `fun`.adaptive.iot.ws

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.iot.ui.space.spaceTreeEditor
import `fun`.adaptive.ui.workspace.Workspace.Companion.wsContext
import `fun`.adaptive.ui.workspace.wsToolPane

@Adaptive
fun wsSpaceToolPane(): AdaptiveFragment {

    val context = fragment().wsContext<AioWsContext>()

    wsToolPane(context.pane(AioWsContext.WSPANE_SPACE_TOOL)) {
        spaceTreeEditor(context.spaceTree)
    }

    return fragment()
}