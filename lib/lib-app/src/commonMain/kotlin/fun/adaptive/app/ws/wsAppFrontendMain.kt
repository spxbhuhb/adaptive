package `fun`.adaptive.app.ws

import `fun`.adaptive.app.ClientApplication
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.ui.snackbar.snackContainer
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.fragments.multiPaneWorkspace

@Adaptive
fun wsAppFrontendMain(application: ClientApplication<MultiPaneWorkspace, BackendWorkspace>): AdaptiveFragment {

    localContext(application.frontendWorkspace) {
        multiPaneWorkspace(application.frontendWorkspace)
    }

    snackContainer()

    return fragment()
}