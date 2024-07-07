/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.auth.api

import hu.simplexion.adaptive.auth.model.Principal
import hu.simplexion.adaptive.auth.model.Role
import hu.simplexion.adaptive.auth.model.RoleContext
import hu.simplexion.adaptive.service.ServiceApi
import hu.simplexion.adaptive.utility.UUID

@ServiceApi
interface RoleApi {

    suspend fun all(): List<Role>

    suspend fun add(role: Role)

    suspend fun update(role: Role)

    suspend fun remove(roleId: UUID<Role>)

    suspend fun addToGroup(roleId: UUID<Role>, groupId: UUID<Role>)

    suspend fun removeFromGroup(roleId: UUID<Role>, groupId: UUID<Role>)

    suspend fun grant(roleId: UUID<Role>, principalId: UUID<Principal>)

    suspend fun revoke(roleId: UUID<Role>, principalId: UUID<Principal>)

    suspend fun rolesOf(principalId: UUID<Principal>, contextId: UUID<RoleContext>?): List<Role>

    suspend fun grantedTo(roleId: UUID<Role>): List<UUID<Principal>>

}