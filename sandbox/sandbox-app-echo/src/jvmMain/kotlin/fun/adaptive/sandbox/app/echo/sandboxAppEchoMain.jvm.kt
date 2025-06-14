package `fun`.adaptive.sandbox.app.echo

import `fun`.adaptive.app.JvmServerApplication.Companion.jvmServer
import `fun`.adaptive.app.app.AppMainModuleServer
import `fun`.adaptive.auth.app.NoAuthServerModule
import `fun`.adaptive.backend.setting.dsl.inline
import `fun`.adaptive.backend.setting.dsl.settings
import `fun`.adaptive.ktor.KtorJvmServerModule
import `fun`.adaptive.sandbox.app.echo.app.EchoServerModule

fun main() {

    settings {
        inline(
            "KTOR_PORT" to 8080,
            "KTOR_WIREFORMAT" to "json"
        )
    }

    jvmServer {
        module { EchoServerModule() } // business function :)
        module { NoAuthServerModule() } // no authentication
        module { KtorJvmServerModule() } // Ktor server
        module { AppMainModuleServer() } // basic app bootstrap
    }
}