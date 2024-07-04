/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.auth.store

import hu.simplexion.adaptive.auth.model.Role
import hu.simplexion.adaptive.exposed.asCommon
import hu.simplexion.adaptive.exposed.asJava
import hu.simplexion.adaptive.exposed.uuidEq
import hu.simplexion.adaptive.utility.UUID
import org.jetbrains.exposed.sql.*

object RoleMembershipTable : Table("auth_role_membership") {

    val group = reference("group", RoleTable)
    val member = reference("member", RoleTable)

    fun all(): List<Pair<UUID<Role>, UUID<Role>>> =
        selectAll()
            .map { Pair(it[group].asCommon(), it[member].asCommon()) }

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