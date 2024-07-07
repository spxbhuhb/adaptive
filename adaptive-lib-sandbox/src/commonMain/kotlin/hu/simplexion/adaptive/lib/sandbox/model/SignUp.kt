/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.sandbox.model

import hu.simplexion.adaptive.adat.Adat

@Adat
class SignUp(
    val name : String = "",
    val email : String = "",
    val password : String = "",
    val verification : String = "",
    val agreement : Boolean = false
)