package `fun`.adaptive.iot.app

import `fun`.adaptive.app.JvmServerApplication.Companion.jvmServer
import `fun`.adaptive.app.server.BasicAppServerModule
import `fun`.adaptive.auth.app.AuthBasicServerModule
import `fun`.adaptive.backend.setting.dsl.inline
import `fun`.adaptive.backend.setting.dsl.propertyFile
import `fun`.adaptive.backend.setting.dsl.settings
import `fun`.adaptive.document.app.DocServerModule
import `fun`.adaptive.iot.lib.zigbee.ZigbeeModule
import `fun`.adaptive.ktor.KtorJvmServerModule
import `fun`.adaptive.lib.util.app.UtilServerModule
import `fun`.adaptive.utility.ensure
import `fun`.adaptive.value.app.ValueServerModule
import `fun`.adaptive.value.persistence.FilePersistence
import kotlinx.io.files.Path

fun main() {

    settings {
        propertyFile { "./etc/aio.properties" }
        inline(
            "KTOR_WIREFORMAT" to "proto",
            "AIO_HISTORY_PATH" to "./var/history"
        )
    }

    jvmServer {
        module { UtilServerModule() }
        module { ValueServerModule("general", FilePersistence(Path("./var/values").ensure(), 2)) }
        module { AuthBasicServerModule() }
        module { DocServerModule() }
        module { IotServerModule() }
        module { ZigbeeModule() }
        module { KtorJvmServerModule() }
        module { BasicAppServerModule() }
    }

}