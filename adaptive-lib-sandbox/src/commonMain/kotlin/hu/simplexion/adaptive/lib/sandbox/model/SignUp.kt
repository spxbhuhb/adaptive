/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.sandbox.model

import hu.simplexion.adaptive.adat.Adat

@Adat
class SignUp(
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var verification: String = "",
    var agreement: Boolean = false
)