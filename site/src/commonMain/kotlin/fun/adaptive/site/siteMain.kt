package `fun`.adaptive.site

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.ui.snackbar.snackContainer
import `fun`.adaptive.ui.workspace.wsFull

@Adaptive
fun siteMain() {

    val workspace = buildWorkspace()

    localContext(workspace) {
        wsFull(workspace)
    }

    snackContainer()
}