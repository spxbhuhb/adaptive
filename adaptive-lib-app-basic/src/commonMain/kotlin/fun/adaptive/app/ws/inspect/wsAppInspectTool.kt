package `fun`.adaptive.app.ws.inspect

import `fun`.adaptive.app.UiClientApplication
import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.wsToolPane

@Adaptive
fun wsAppInspectTool(pane: WsPane<*, *>): AdaptiveFragment {

    val app = fragment().firstContext<UiClientApplication<*,*>>()

    wsToolPane(pane) {
        row {
            maxSize .. verticalScroll
            gap { 16.dp}
            modules(app)
            session(app)
        }
    }

    return fragment()
}

@Adaptive
private fun modules(app : UiClientApplication<*,*>) {
    column {
        h2("Modules")
        for (module in app.modules) {
            text(module::class.simpleName ?: "<no-name>")
        }
    }
}

@Adaptive
private fun session(app : UiClientApplication<*,*>) {
    val session = app.appData.sessionOrNull

    column {
        h2("Session")

        text("Session Id: ${session?.uuid ?: "<no-session>"}")
        text("Principal ID: ${session?.principalOrNull}")
        text("Roles: ${session?.roles?.joinToString()}")
    }
}