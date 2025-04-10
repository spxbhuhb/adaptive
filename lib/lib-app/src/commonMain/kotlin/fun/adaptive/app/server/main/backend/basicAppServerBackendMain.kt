package `fun`.adaptive.app.server.main.backend

import `fun`.adaptive.app.ServerApplication
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.runtime.ServerWorkspace

@Adaptive
fun basicAppServerBackendMain(): AdaptiveFragment {

    val ws = fragment().firstContext<ServerApplication<*>>().workspace as ServerWorkspace

    for (s in ws.services) {
        service { s }
    }

    for (w in ws.workers) {
        worker { w }
    }

    return fragment()
}