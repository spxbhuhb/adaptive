package `fun`.adaptive.grove.doc.ui

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.mpw.fragments.toolPane
import `fun`.adaptive.ui.viewbackend.viewBackend

@Adaptive
fun docToolPane(): AdaptiveFragment {

    val viewBackend = viewBackend(DocToolViewBackend::class)

    toolPane(viewBackend.paneDef) {
        tree(viewBackend.tree.treeBackend)
    }

    return fragment()
}