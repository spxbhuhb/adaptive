package `fun`.adaptive.app.server

import `fun`.adaptive.runtime.AbstractServerApplication
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.fragment

@Adaptive
fun serverBackendMain(): AdaptiveFragment {

    val ws = fragment().firstContext<AbstractServerApplication<*,*>>().backendWorkspace

    for (s in ws.services) {
        service { s }
    }

    for (w in ws.workers) {
        worker { w }
    }

    return fragment()
}