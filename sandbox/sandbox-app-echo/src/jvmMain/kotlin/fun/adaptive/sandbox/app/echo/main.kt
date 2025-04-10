package `fun`.adaptive.sandbox.app.echo

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
            "KTOR_WIREFORMAT" to "proto"
        )
    }

    jvmServer {
        module { EchoServerModule() } // business function :)
        module { NoAuthServerModule() } // no authentication
        module { KtorJvmServerModule() } // Ktor server
        module { BasicAppServerModule() } // basic app bootstrap
    }
}