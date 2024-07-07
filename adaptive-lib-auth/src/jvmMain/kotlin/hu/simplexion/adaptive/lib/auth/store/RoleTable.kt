/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.auth.store

import hu.simplexion.adaptive.auth.model.Role
import hu.simplexion.adaptive.exposed.AdatEntityTable
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.select

object RoleTable : AdatEntityTable<Role, RoleTable>("auth_role", columnName = "uuid") {

    val name = varchar("name", 100)
    val context = reference("context", RoleContextTable).index()
    val group = bool("group")
    val displayOrder = integer("display_order")

    fun getByContext(name: String): List<Role> =
        join(RoleContextTable, JoinType.INNER, context, RoleContextTable.id)
            .select { RoleContextTable.name eq name }
            .map { fromRow(it) }

}