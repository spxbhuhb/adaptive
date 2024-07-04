/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.auth.service

import hu.simplexion.adaptive.adat.ensureValid
import hu.simplexion.adaptive.auth.api.RoleApi
import hu.simplexion.adaptive.auth.model.Principal
import hu.simplexion.adaptive.auth.model.Role
import hu.simplexion.adaptive.auth.model.RoleContext
import hu.simplexion.adaptive.auth.model.RoleGrant
import hu.simplexion.adaptive.lib.auth.context.ensureAll
import hu.simplexion.adaptive.lib.auth.context.ensureOneOf
import hu.simplexion.adaptive.lib.auth.store.*
import hu.simplexion.adaptive.server.builtin.ServiceImpl
import hu.simplexion.adaptive.utility.UUID

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

        roles.add { role }
    }

    override suspend fun update(role: Role) {
        ensureAll(*updateRoles)
        ensureValid(role)

        history(role)

        roles.update { role }
    }

    override suspend fun remove(roleId: UUID<Role>) {
        ensureAll(*updateRoles)

        history(roleId)

        roleGrants.remove { roleId }
        roles.remove { roleId }
    }

    override suspend fun grant(roleId: UUID<Role>, principalId: UUID<Principal>) {
        ensureOneOf(*grantRoles)

        history(principalId, roleId)

        roleGrants.add(RoleGrant(principalId, roleId))
    }

    override suspend fun revoke(roleId: UUID<Role>, principalId: UUID<Principal>) {
        ensureOneOf(*grantRoles)

        history(principalId, roleId)

        roleGrants.remove(roleId, principalId)
    }

    override suspend fun rolesOf(principalId: UUID<Principal>, contextId: UUID<RoleContext>?): List<Role> {
        ensureOneOf(*getRoles)

        return roleGrants.rolesOf(principalId, contextId)
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