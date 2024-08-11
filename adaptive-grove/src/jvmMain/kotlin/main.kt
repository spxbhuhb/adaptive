/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.email.email
import `fun`.adaptive.exposed.inMemoryH2
import `fun`.adaptive.ktor.ktor
import `fun`.adaptive.lib.auth.auth
import `fun`.adaptive.server.server
import `fun`.adaptive.server.setting.dsl.propertyFile
import `fun`.adaptive.server.setting.dsl.settings

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