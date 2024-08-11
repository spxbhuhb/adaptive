/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.api

import `fun`.adaptive.auth.model.Credential
import `fun`.adaptive.auth.model.Principal
import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.utility.UUID

@ServiceApi
interface PrincipalApi {

    suspend fun all(): List<Principal>

    suspend fun addPrincipal(principal: Principal, activated: Boolean, activationKey: String?)

    suspend fun addCredential(credential: Credential, currentCredential: Credential? = null)

    suspend fun get(principalId: UUID<Principal>): Principal

    suspend fun activate(credential: Credential, key: Credential)

    suspend fun resetPassword(credential: Credential, key: Credential)

    suspend fun setActivated(principalId: UUID<Principal>, activated: Boolean)

    suspend fun setLocked(principalId: UUID<Principal>, locked: Boolean)

}