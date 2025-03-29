package `fun`.adaptive.cookbook.app.echo

import `fun`.adaptive.app.JvmServerApplication.Companion.jvmServer
import `fun`.adaptive.app.server.BasicAppServerModule
import `fun`.adaptive.auth.app.NoAuthServerModule
import `fun`.adaptive.backend.setting.dsl.inline
import `fun`.adaptive.backend.setting.dsl.settings
import `fun`.adaptive.ktor.KtorJvmServerModule

fun main() {

    settings {
        inline(
            "KTOR_PORT" to 8080,
            "KTOR_WIREFORMAT" to "json"
        )
    }

    jvmServer {
        // this module is the actual functionality provided by the server

        module { EchoServerModule() }

        // these modules provide a very minimal server setup:
        // - no authentication
        // - Ktor server on 8080

        module { NoAuthServerModule() }
        module { KtorJvmServerModule() }
        module { BasicAppServerModule() }
    }
}