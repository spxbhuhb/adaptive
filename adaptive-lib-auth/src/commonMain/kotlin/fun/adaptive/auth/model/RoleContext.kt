/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.AdatEntity
import `fun`.adaptive.utility.UUID

@Adat
class RoleContext(
    override val id: UUID<RoleContext>,
    var name: String? = null,
    var type: String? = null
) : AdatEntity<RoleContext> {
    companion object : AdatCompanion<RoleContext>
}