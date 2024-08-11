/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.lib.auth.store

import `fun`.adaptive.auth.model.Principal
import `fun`.adaptive.auth.model.Role
import `fun`.adaptive.auth.model.RoleContext
import `fun`.adaptive.auth.model.RoleGrant
import `fun`.adaptive.exposed.AdatTable
import `fun`.adaptive.exposed.asCommon
import `fun`.adaptive.exposed.uuidEq
import `fun`.adaptive.utility.UUID
import org.jetbrains.exposed.sql.*

object RoleGrantTable : AdatTable<RoleGrant, RoleGrantTable>("auth_role_grant") {

    val principal = reference("principal", PrincipalTable).index()
    val role = reference("role", RoleTable)

    fun remove(roleId: () -> UUID<Role>) =
        remove(roleId())

    fun remove(roleId: UUID<Role>) =
        deleteWhere { role uuidEq roleId }

    fun remove(roleId: UUID<Role>, principalId: UUID<Principal>) {
        deleteWhere {
            (role uuidEq roleId) and (principal uuidEq principalId)
        }
    }

    operator fun minusAssign(roleId: UUID<Role>) {
        remove(roleId)
    }

    fun rolesOf(principalId: UUID<Principal>, contextId: UUID<RoleContext>?): List<Role> {

        val condition = if (contextId == null) {
            Op.build { principal uuidEq principalId }
        } else {
            Op.build { (principal uuidEq principalId) and (RoleTable.context uuidEq contextId) }
        }

        return this
            .join(RoleTable, JoinType.INNER, additionalConstraint = { role eq RoleTable.id })
            .select(condition)
            .map { RoleTable.fromRow(it) }
    }

    fun hasRole(roleId: UUID<Role>, principalId: UUID<Principal>): Boolean =
        select { (principal uuidEq principalId) and (role uuidEq roleId) }
            .empty()
            .not()

    fun grantedTo(roleId: UUID<Role>): List<UUID<Principal>> =
        select { role uuidEq roleId }
            .map { it[principal].asCommon() }

}