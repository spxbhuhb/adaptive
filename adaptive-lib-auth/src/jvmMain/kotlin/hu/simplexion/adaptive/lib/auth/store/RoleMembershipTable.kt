/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.auth.store

import hu.simplexion.adaptive.auth.model.Role
import hu.simplexion.adaptive.auth.model.RoleMembership
import hu.simplexion.adaptive.exposed.AdatTable
import hu.simplexion.adaptive.exposed.asJava
import hu.simplexion.adaptive.exposed.uuidEq
import hu.simplexion.adaptive.utility.UUID
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

object RoleMembershipTable : AdatTable<RoleMembership, RoleMembershipTable>("auth_role_membership") {

    val group = reference("group", RoleTable)
    val member = reference("member", RoleTable)

    fun add(itemId: UUID<Role>, groupId: UUID<Role>) {
        val jvmItem = itemId.asJava()
        val jvmGroup = groupId.asJava()
        if (select { (member eq jvmItem) and (group eq jvmGroup) }.count() > 0) return
        insert {
            it[group] = jvmGroup
            it[member] = jvmItem
        }
    }

    fun remove(itemId: UUID<Role>, groupId: UUID<Role>) {
        deleteWhere { (member uuidEq itemId) and (group uuidEq groupId) }
    }

}