/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.cookbook.auth.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.properties

@Adat
class SignIn(
    val email: String = "",
    val password: String = "",
    val remember: Boolean = false
) {

    override fun descriptor() {
        properties {
            email blank false pattern "^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"
            password secret true blank false
        }
    }

}