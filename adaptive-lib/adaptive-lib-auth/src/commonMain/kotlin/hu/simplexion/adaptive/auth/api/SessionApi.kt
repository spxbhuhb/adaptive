/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.auth.api

import hu.simplexion.adaptive.auth.model.Principal
import hu.simplexion.adaptive.auth.model.Role
import hu.simplexion.adaptive.auth.model.Session
import hu.simplexion.adaptive.service.ServiceApi
import hu.simplexion.adaptive.utility.UUID

@ServiceApi
interface SessionApi {

    suspend fun owner(): UUID<Principal>?

    suspend fun roles(): List<UUID<Role>>

    suspend fun login(name: String, password: String): Session

    suspend fun activateSession(securityCode: String): Session

    suspend fun getSession(): Session?

    suspend fun logout()

    suspend fun logout(session: UUID<Session>)

    suspend fun list(): List<Session>

}