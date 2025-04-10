/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.model

import `fun`.adaptive.adat.Adat

@Adat
data class RoleSpec(
    var context: String? = null,
    var group: Boolean = false,
    var displayOrder: Int = 0
)