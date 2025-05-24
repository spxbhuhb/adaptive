package `fun`.adaptive.ui.example

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.api.findContext
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.ui.workspace.wsFull

@Adaptive
fun createMultiPaneWorkspace() {

    val workspace = MultiPaneWorkspace(adapter().backend)

    localContext(workspace) {
        wsFull(workspace)
    }

}

@Adaptive
fun findWorkspace() {
    val workspace = fragment().findContext<MultiPaneWorkspace>()
}