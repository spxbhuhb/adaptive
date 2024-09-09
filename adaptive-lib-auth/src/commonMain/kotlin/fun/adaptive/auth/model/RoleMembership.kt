/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.utility.UUID

@Adat
class RoleMembership(
    var group: UUID<Role>,
    var member: UUID<Role>
)