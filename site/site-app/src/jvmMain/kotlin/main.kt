/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.app.JvmServerApplication.Companion.jvmServer
import `fun`.adaptive.app.server.BasicAppServerModule
import `fun`.adaptive.auth.app.NoAuthServerModule
import `fun`.adaptive.backend.setting.dsl.propertyFile
import `fun`.adaptive.backend.setting.dsl.settings
import `fun`.adaptive.ktor.KtorJvmServerModule
import `fun`.adaptive.lib.util.app.UtilServerModule
import `fun`.adaptive.value.app.ValueServerModule
import `fun`.adaptive.value.persistence.FilePersistence
import kotlinx.io.files.Path

fun main() {

    settings {
        propertyFile(optional = false) { "./etc/site.properties" }
    }

    jvmServer {
        module { UtilServerModule() }
        module { ValueServerModule(FilePersistence(Path("./var/values"))) }
        module { NoAuthServerModule() }
        module { KtorJvmServerModule() }
        module { BasicAppServerModule() }
    }

}