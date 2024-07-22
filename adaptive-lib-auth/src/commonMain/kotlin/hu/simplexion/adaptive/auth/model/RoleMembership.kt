/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.auth.model

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.AdatCompanion
import hu.simplexion.adaptive.utility.UUID

@Adat
class RoleMembership(
    var group: UUID<Role>,
    var member: UUID<Role>
) : AdatClass<RoleMembership> {
    companion object : AdatCompanion<RoleMembership>
}