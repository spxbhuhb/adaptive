package `fun`.adaptive.app.ui.mpw.inspect

import `fun`.adaptive.app.ui.common.devtool.inspect.appInspect
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.mpw.backends.UnitPaneViewBackend
import `fun`.adaptive.ui.mpw.fragments.toolPane
import `fun`.adaptive.ui.viewbackend.viewBackend

@Adaptive
fun appInspectTool(): AdaptiveFragment {

    val viewBackend = viewBackend(UnitPaneViewBackend::class)

    toolPane(viewBackend) {
        appInspect()
    }

    return fragment()
}