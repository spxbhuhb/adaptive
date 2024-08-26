/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.cookbook.auth.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.descriptor.api.properties

@Adat
class SignUp(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val verification: String = "",
    val agreement: Boolean = false
) : AdatClass<SignUp> {

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