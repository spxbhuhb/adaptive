package `fun`.adaptive.app.ws.inspect

import `fun`.adaptive.app.ClientApplication
import `fun`.adaptive.auth.app.AuthAppContext.Companion.authContext
import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.mpw.backends.UnitPaneViewBackend
import `fun`.adaptive.ui.mpw.fragments.toolPane
import `fun`.adaptive.ui.viewbackend.viewBackend
import `fun`.adaptive.wireformat.WireFormatRegistry

@Adaptive
fun wsAppInspectTool(): AdaptiveFragment {

    val viewBackend = viewBackend(UnitPaneViewBackend::class)
    val app = fragment().firstContext<ClientApplication<*,*>>()

    toolPane(viewBackend) {
        row {
            maxSize .. verticalScroll
            gap { 16.dp}
            modules(app)
            session(app)
            wireFormats()
        }
    }

    return fragment()
}

@Adaptive
private fun modules(app : ClientApplication<*,*>) {
    column {
        h2("Modules")
        for (module in app.modules) {
            text(module::class.simpleName ?: "<no-name>")
        }
    }
}

@Adaptive
private fun session(app : ClientApplication<*,*>) {
    val session = app.authContext.sessionOrNull

    column {
        h2("Session")

        text("Session Id: ${session?.uuid ?: "<no-session>"}")
        text("Principal ID: ${session?.principalOrNull}")
        text("Roles: ${session?.roles?.joinToString()}")
    }
}

@Adaptive
private fun wireFormats() {

    column {
        h2("Wireformat Registry")
        for (entry in WireFormatRegistry.entries()) {
            text("${entry.key} : ${entry.value::class.simpleName}")
        }
    }
}