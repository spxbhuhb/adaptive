/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.app.basic.auth.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.properties

@Adat
class BasicSignIn(
    val login: String = "",
    val password: String = "",
    val remember: Boolean = false
) {

    override fun descriptor() {
        properties {
            login blank false
            password secret true blank false
        }
    }

}