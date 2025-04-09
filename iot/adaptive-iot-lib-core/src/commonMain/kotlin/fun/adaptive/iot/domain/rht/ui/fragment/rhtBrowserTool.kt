package `fun`.adaptive.iot.domain.rht.ui.fragment

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.domain.rht.ui.controller.RhtBrowserToolController
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.wsToolPane

@Adaptive
fun rhtBrowserTool(pane: WsPane<Unit, RhtBrowserToolController>): AdaptiveFragment {

    val observed = valueFrom { pane.controller.treeViewModel }

    wsToolPane(pane) {
        tree(observed)
    }

    return fragment()
}