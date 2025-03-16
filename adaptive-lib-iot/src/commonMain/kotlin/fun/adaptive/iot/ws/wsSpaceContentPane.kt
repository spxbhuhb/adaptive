package `fun`.adaptive.iot.ws

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.iot.ui.space.spaceEditor
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.workspace.Workspace.Companion.wsContext
import `fun`.adaptive.ui.workspace.model.WsPane

@Adaptive
fun wsSpaceContentPane(): AdaptiveFragment {
    val context = fragment().wsContext<AioWsContext>()

    spaceEditor(context.spaceTree)

    return fragment()
}