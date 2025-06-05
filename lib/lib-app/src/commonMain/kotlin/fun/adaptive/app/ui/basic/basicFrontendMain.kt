package `fun`.adaptive.app.ui.basic

import `fun`.adaptive.app.ClientApplication
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.runtime.FrontendWorkspace
import `fun`.adaptive.ui.misc.todo

@Adaptive
fun basicFrontendMain(application: ClientApplication<FrontendWorkspace,*>): AdaptiveFragment {

    localContext(application.frontendWorkspace) {
        todo("override the app frontend fragment key")
    }

    return fragment()
}