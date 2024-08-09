/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.email.email
import hu.simplexion.adaptive.exposed.inMemoryH2
import hu.simplexion.adaptive.ktor.ktor
import hu.simplexion.adaptive.lib.auth.auth
import hu.simplexion.adaptive.server.server
import hu.simplexion.adaptive.server.setting.dsl.propertyFile
import hu.simplexion.adaptive.server.setting.dsl.settings

fun main() {

    server(wait = true) {

        settings {
            propertyFile(optional = false) { "./etc/grove.properties" }
        }

        inMemoryH2()

        auth()
        email()
        ktor()
    }

}