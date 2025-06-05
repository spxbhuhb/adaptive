package `fun`.adaptive.value.example

import `fun`.adaptive.app.JvmServerApplication.Companion.jvmServer
import `fun`.adaptive.app.app.AppMainModuleServer
import `fun`.adaptive.auth.app.NoAuthServerModule
import `fun`.adaptive.ktor.KtorJvmServerModule
import `fun`.adaptive.lib.util.app.UtilModule
import `fun`.adaptive.persistence.ensure
import `fun`.adaptive.value.app.ValueServerModule
import `fun`.adaptive.value.persistence.FilePersistence
import kotlinx.io.files.Path

fun main() {
    jvmServer {
        module { UtilModule() }
        module { ValueServerModule(FilePersistence(Path("./var/values").ensure(), 2)) }
        module { NoAuthServerModule() } // no authentication
        module { KtorJvmServerModule() }
        module { AppMainModuleServer() }
    }
}