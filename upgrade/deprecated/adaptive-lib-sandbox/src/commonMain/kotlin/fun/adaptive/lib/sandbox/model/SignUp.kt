/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.lib.sandbox.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass

@Adat
class SignUp(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val verification: String = "",
    val agreement: Boolean = false
)