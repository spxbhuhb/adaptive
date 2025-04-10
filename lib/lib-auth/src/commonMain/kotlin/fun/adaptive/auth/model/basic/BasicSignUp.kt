/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.model.basic

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.properties

@Adat
class BasicSignUp(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val verification: String = "",
    val agreement: Boolean = false
) {

    override fun descriptor() {
        properties {
            name blank false minLength 5 maxLength 100
            email blank false pattern "^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"
            password secret true blank false
            verification secret true blank false
            agreement value true
        }
    }

}