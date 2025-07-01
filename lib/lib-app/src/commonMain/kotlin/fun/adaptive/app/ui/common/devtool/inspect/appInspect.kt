package `fun`.adaptive.app.ui.common.devtool.inspect

import `fun`.adaptive.app.ClientApplication
import `fun`.adaptive.auth.app.AuthAppContext.Companion.authContext
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.button.button
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.platform.clipboard.copyToClipboard
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.wireformat.WireFormatRegistry

@Adaptive
fun appInspect(): AdaptiveFragment {

    val app = fragment().firstContext<ClientApplication<*, *>>()
    var generatedUuid = uuid4<Any>()

    column {
        maxSize .. verticalScroll
        row {
            button("Copy values to clipboard") .. onClick { event ->
                copyToClipboard(app.backend.firstImpl<AvValueWorker>().store.dump())
            }
            column {
                button("Copy & generate new") .. onClick { event ->
                    copyToClipboard(generatedUuid.toString())
                    generatedUuid = uuid4<Any>()
                }
                text("Generated UUID v4: $generatedUuid")
            }
        }
        row {
            gap { 16.dp }
            modules(app)
            session(app)
            wireFormats()
        }
    }

    return fragment()
}

@Adaptive
private fun modules(app: ClientApplication<*, *>) {
    column {
        h2("Modules")
        for (module in app.modules) {
            text(module::class.simpleName ?: "<no-name>")
        }
    }
}

@Adaptive
private fun session(app: ClientApplication<*, *>) {
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