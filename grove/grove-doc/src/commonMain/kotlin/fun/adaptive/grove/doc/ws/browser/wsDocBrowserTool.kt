package `fun`.adaptive.grove.doc.ws.browser

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.mpw.model.Pane
import `fun`.adaptive.ui.mpw.fragments.toolPane

@Adaptive
fun wsDocBrowserTool(pane: Pane<DocBrowserToolViewBackend>): AdaptiveFragment {

    val observed = valueFrom { pane.viewBackend.tree.treeBackend }

    toolPane(pane) {
        tree(observed)
    }

    return fragment()
}