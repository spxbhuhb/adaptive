package `fun`.adaptive.app.client.basic.main.frontend

import `fun`.adaptive.app.ClientApplication
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.misc.todo
import `fun`.adaptive.ui.workspace.Workspace

@Adaptive
fun basicClientFrontendMain(application: ClientApplication<Workspace>): AdaptiveFragment {

    localContext(application.workspace) {
        todo("override the app frontend fragment key")
    }

    return fragment()
}