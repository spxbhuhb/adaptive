package `fun`.adaptive.ui.mpw.example.tree

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.mpw.fragments.toolPane
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.viewbackend.viewBackend

@Adaptive
fun exampleToolPane(): AdaptiveFragment {

    val viewBackend = viewBackend(ExampleTreeToolViewBackend::class)

    toolPane(viewBackend) {
        tree(viewBackend.tree.treeBackend)
    }

    return fragment()
}