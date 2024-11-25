/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.AdatEntity
import `fun`.adaptive.utility.UUID

@Adat
class Role(
    override val id: UUID<Role>,
    var name: String,
    var context: UUID<RoleContext>? = null,
    var group: Boolean = false,
    var displayOrder: Int = 0,
) : AdatEntity<Role> {

    companion object : AdatCompanion<Role>

}