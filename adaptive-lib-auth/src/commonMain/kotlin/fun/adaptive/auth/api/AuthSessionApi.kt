/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.api

import `fun`.adaptive.auth.model.AuthPrincipalId
import `fun`.adaptive.auth.model.AuthRoleId
import `fun`.adaptive.auth.model.Session
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface AuthSessionApi {

    suspend fun owner(): AuthPrincipalId?

    suspend fun roles(): Set<AuthRoleId>

    suspend fun login(name: String, password: String): Session

    suspend fun activateSession(securityCode: String): Session

    suspend fun getSession(): Session?

    suspend fun logout()

}