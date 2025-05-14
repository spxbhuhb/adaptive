package `fun`.adaptive.app.ws

import `fun`.adaptive.app.ClientApplication
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.snackbar.snackContainer
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.ui.workspace.wsFull

@Adaptive
fun wsAppFrontendMain(application: ClientApplication<MultiPaneWorkspace>): AdaptiveFragment {

    localContext(application.workspace) {
        wsFull(application.workspace)
    }

    snackContainer()

    return fragment()
}