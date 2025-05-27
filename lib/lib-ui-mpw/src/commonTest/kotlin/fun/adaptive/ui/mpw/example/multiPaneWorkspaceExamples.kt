package `fun`.adaptive.ui.mpw.example

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.api.findContext
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.fragments.multiPaneWorkspace

@Adaptive
fun createMultiPaneWorkspace() {

    val workspace = MultiPaneWorkspace(adapter().backend)

    localContext(workspace) {
        multiPaneWorkspace(workspace)
    }

}

@Adaptive
fun findWorkspace() {
    val workspace = fragment().findContext<MultiPaneWorkspace>()
}