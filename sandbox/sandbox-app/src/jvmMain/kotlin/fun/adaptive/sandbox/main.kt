package `fun`.adaptive.sandbox

import `fun`.adaptive.app.JvmServerApplication.Companion.jvmServer
import `fun`.adaptive.app.server.BasicAppServerModule
import `fun`.adaptive.auth.app.AuthServerModule
import `fun`.adaptive.auth.context.ensureLoggedIn
import `fun`.adaptive.backend.setting.dsl.inline
import `fun`.adaptive.backend.setting.dsl.settings
import `fun`.adaptive.ktor.KtorJvmServerModule
import `fun`.adaptive.lib.util.app.UtilServerModule
import `fun`.adaptive.utility.ensure
import `fun`.adaptive.value.app.ValueServerModule
import `fun`.adaptive.value.persistence.FilePersistence
import kotlinx.io.files.Path

fun main() {

    settings {
        inline(
            "KTOR_PORT" to 8080,
            "KTOR_WIREFORMAT" to "json",
            "AIO_HISTORY_PATH" to "./var/history"
        )
    }

    jvmServer {
        module { UtilServerModule() }
        module { ValueServerModule("general",  { ensureLoggedIn() }, FilePersistence(Path("./var/values").ensure(), 2)) }
        module { AuthServerModule() }
        module { KtorJvmServerModule() }
        module { BasicAppServerModule() }
    }
}