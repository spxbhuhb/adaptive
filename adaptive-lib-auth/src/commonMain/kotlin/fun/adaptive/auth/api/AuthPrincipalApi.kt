/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.api

import `fun`.adaptive.auth.model.AuthPrincipal
import `fun`.adaptive.auth.model.AuthPrincipalId
import `fun`.adaptive.auth.model.Credential
import `fun`.adaptive.auth.model.PrincipalSpec
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface AuthPrincipalApi {

    suspend fun all(): List<AuthPrincipal>

    suspend fun addPrincipal(
        name: String,
        spec: PrincipalSpec,
        activationKey: String?
    )

    suspend fun addCredential(
        principalId: AuthPrincipalId,
        credential: Credential,
        currentCredential: Credential? = null
    )

    suspend fun getOrNull(principalId: AuthPrincipalId): AuthPrincipal?

    suspend fun activate(principalId: AuthPrincipalId, credential: Credential, key: Credential)

    suspend fun resetPassword(principalId: AuthPrincipalId, credential: Credential, key: Credential)

    suspend fun setActivated(principalId: AuthPrincipalId, activated: Boolean)

    suspend fun setLocked(principalId: AuthPrincipalId, locked: Boolean)

}