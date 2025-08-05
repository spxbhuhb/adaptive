/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.app.JvmServerApplication.Companion.jvmServer
import `fun`.adaptive.app.app.AppMainModuleServer
import `fun`.adaptive.auth.app.NoAuthServerModule
import `fun`.adaptive.backend.setting.dsl.propertyFile
import `fun`.adaptive.backend.setting.dsl.settings
import `fun`.adaptive.grove.doc.app.GroveDocModuleServer
import `fun`.adaptive.ktor.KtorJvmServerModule
import `fun`.adaptive.lib.util.app.UtilModule
import `fun`.adaptive.persistence.ensure
import `fun`.adaptive.runtime.AppAboutData
import `fun`.adaptive.sandbox.app.CookbookModule
import `fun`.adaptive.site.app.SiteModuleServer
import `fun`.adaptive.site.appVersion
import `fun`.adaptive.value.app.ValueServerModule
import `fun`.adaptive.value.persistence.FilePersistence
import kotlinx.io.files.Path

fun main() {

    settings {
        propertyFile(optional = false) { "./etc/site.properties" }
    }

    jvmServer(AppAboutData(appVersion)) {
        module { UtilModule() }
        module { ValueServerModule(FilePersistence(Path("./var/values").ensure())) }
        module { NoAuthServerModule() }
        module { KtorJvmServerModule() }
        module { GroveDocModuleServer() }
        module { CookbookModule() }
        module { SiteModuleServer() }
        module { AppMainModuleServer() }
    }

}