/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.auth.store

import hu.simplexion.adaptive.auth.model.RoleContext
import hu.simplexion.adaptive.exposed.AdatEntityTable

object RoleContextTable : AdatEntityTable<RoleContext, RoleContextTable>("auth_role_context") {

    val name = varchar("name", 100).nullable()
    val type = varchar("name", 100).nullable()

}