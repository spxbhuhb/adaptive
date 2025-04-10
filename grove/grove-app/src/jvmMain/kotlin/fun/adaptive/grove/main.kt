/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.grove

import `fun`.adaptive.ktor.ktor
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.setting.dsl.propertyFile
import `fun`.adaptive.backend.setting.dsl.settings

fun main() {

    backend(wait = true) {

        settings {
            propertyFile(optional = false) { "./etc/grove.properties" }
        }

        ktor()
    }

}