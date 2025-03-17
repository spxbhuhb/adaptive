package `fun`.adaptive.ui.workspace

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.workspace.model.WsPane

@Adaptive
fun wsEmptyCenterPane(pane : WsPane<*>) : AdaptiveFragment {
    return fragment()
}