/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.auth.api

import hu.simplexion.adaptive.auth.model.Credentials
import hu.simplexion.adaptive.auth.model.Principal
import hu.simplexion.adaptive.auth.model.Role
import hu.simplexion.adaptive.service.ServiceApi
import hu.simplexion.adaptive.utility.UUID

@ServiceApi
interface PrincipalApi {

    suspend fun list(): List<Principal>

    suspend fun add(principal: Principal, activated: Boolean, activationKey: String?, roles: List<UUID<Role>>): UUID<Principal>

    suspend fun add(credentials: Credentials, currentCredentials: Credentials? = null)

    suspend fun get(uuid: UUID<Principal>): Principal

    suspend fun activate(credentials: Credentials, key: Credentials): String

    suspend fun resetPassword(credentials: Credentials, key: Credentials): String

    suspend fun setActivated(uuid: UUID<Principal>, activated: Boolean)

    suspend fun setLocked(uuid: UUID<Principal>, locked: Boolean)

}