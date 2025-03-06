/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.lib.auth.store

import `fun`.adaptive.auth.model.Role
import `fun`.adaptive.exposed.AdatEntityTable
import `fun`.adaptive.exposed.ExposedAdatTable
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.select

@ExposedAdatTable
object RoleTable : AdatEntityTable<Role, RoleTable>("auth_role", columnName = "uuid") {

    val name = varchar("name", 100)
    val context = reference("context", RoleContextTable).nullable().index()
    val group = bool("group")
    val displayOrder = integer("display_order")

    fun getByContext(name: String): List<Role> =
        join(RoleContextTable, JoinType.INNER, context, id)
            .select { RoleContextTable.name eq name }
            .map { fromRow(it) }

    fun getByNameOrNull(name : String) : Role? =
        select { RoleTable.name eq name }.firstOrNull()?.let { fromRow(it) }

}