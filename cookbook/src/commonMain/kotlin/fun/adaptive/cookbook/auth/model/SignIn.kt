/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.cookbook.auth.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass

@Adat
class SignIn(
    val email: String = "",
    val password: String = "",
    val remember: Boolean = false
) : AdatClass<SignIn>