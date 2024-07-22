/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.auth.model

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatCompanion
import hu.simplexion.adaptive.adat.AdatEntity
import hu.simplexion.adaptive.utility.UUID

@Adat
class Role(
    override val id: UUID<Role>,
    var context: String? = null,
    var group: Boolean = false,
    var displayOrder: Int = 0
) : AdatEntity<Role> {

    companion object : AdatCompanion<Role>

}