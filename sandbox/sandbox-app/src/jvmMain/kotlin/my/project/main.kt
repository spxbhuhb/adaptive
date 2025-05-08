package my.project

import `fun`.adaptive.app.JvmServerApplication.Companion.jvmServer
import `fun`.adaptive.app.server.BasicAppServerModule
import `fun`.adaptive.auth.app.NoAuthServerModule
import `fun`.adaptive.backend.setting.dsl.inline
import `fun`.adaptive.backend.setting.dsl.propertyFile
import `fun`.adaptive.backend.setting.dsl.settings
import `fun`.adaptive.ktor.KtorJvmServerModule

fun main() {

    settings {
        propertyFile { "./etc/my.project.properties" }
        inline(
            "KTOR_PORT" to 8080,
            "KTOR_WIREFORMAT" to "json",
        )
    }

    jvmServer {
        module { NoAuthServerModule() } // no authentication
        module { KtorJvmServerModule() }
        module { BasicAppServerModule() }
    }
}