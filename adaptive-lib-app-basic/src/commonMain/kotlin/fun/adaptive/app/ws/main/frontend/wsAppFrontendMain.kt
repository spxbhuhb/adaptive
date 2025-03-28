package `fun`.adaptive.app.ws.main.frontend

import `fun`.adaptive.app.UiClientApplication
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.snackbar.snackContainer
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.wsFull

@Adaptive
fun wsAppFrontendMain(application: UiClientApplication<Workspace,*>): AdaptiveFragment {

    localContext(application.workspace) {
        wsFull(application.workspace)
    }

    snackContainer()

    return fragment()
}