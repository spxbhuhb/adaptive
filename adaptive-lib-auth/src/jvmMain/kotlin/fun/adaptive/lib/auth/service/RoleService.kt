/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.lib.auth.service

import `fun`.adaptive.adat.ensureValid
import `fun`.adaptive.auth.api.RoleApi
import `fun`.adaptive.auth.context.ensureAll
import `fun`.adaptive.auth.context.ensureOneOf
import `fun`.adaptive.auth.model.Principal
import `fun`.adaptive.auth.model.Role
import `fun`.adaptive.auth.model.RoleContext
import `fun`.adaptive.auth.model.RoleGrant
import `fun`.adaptive.lib.auth.store.*
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.utility.UUID

class RoleService : RoleApi, ServiceImpl<RoleService> {

    companion object {
        var addRoles = emptyArray<UUID<Role>>()
        var getRoles = emptyArray<UUID<Role>>()
        var grantRoles = emptyArray<UUID<Role>>()
        var updateRoles = emptyArray<UUID<Role>>()
    }

    override suspend fun all(): List<Role> {
        ensureOneOf(*getRoles)

        return RoleTable.all()
    }

    override suspend fun add(role: Role) {
        ensureOneOf(*addRoles)
        ensureValid(role)

        history(role)

        roles += role
    }

    override suspend fun update(role: Role) {
        ensureAll(*updateRoles)
        ensureValid(role)

        history(role)

        roles %= role
    }

    override suspend fun remove(roleId: UUID<Role>) {
        ensureAll(*updateRoles)

        history(roleId)

        roleGrants -= roleId
        roles -= roleId
    }

    override suspend fun grant(roleId: UUID<Role>, principalId: UUID<Principal>) {
        ensureOneOf(*grantRoles)

        history(principalId, roleId)

        roleGrants += RoleGrant(principalId, roleId)
    }

    override suspend fun revoke(roleId: UUID<Role>, principalId: UUID<Principal>) {
        ensureOneOf(*grantRoles)

        history(principalId, roleId)

        roleGrants.remove(roleId, principalId)
    }

    override suspend fun rolesOf(principalId: UUID<Principal>, context: String?): List<Role> {
        ensureOneOf(*getRoles)

        return roleGrants.rolesOf(principalId, context)
    }

    override suspend fun grantedTo(roleId: UUID<Role>): List<UUID<Principal>> {
        ensureOneOf(*getRoles)

        return roleGrants.grantedTo(roleId)
    }

    override suspend fun addToGroup(roleId: UUID<Role>, groupId: UUID<Role>) {
        ensureOneOf(*updateRoles)

        history(roleId, groupId)

        roleMemberships.add(roleId, groupId)
    }

    override suspend fun removeFromGroup(roleId: UUID<Role>, groupId: UUID<Role>) {
        ensureOneOf(*updateRoles)

        history(roleId, groupId)

        roleMemberships.remove(roleId, groupId)
    }

}