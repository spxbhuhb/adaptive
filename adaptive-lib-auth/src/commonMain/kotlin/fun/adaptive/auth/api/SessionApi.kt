/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.api

import `fun`.adaptive.auth.model.Principal
import `fun`.adaptive.auth.model.Role
import `fun`.adaptive.auth.model.Session
import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.utility.UUID

@ServiceApi
interface SessionApi {

    suspend fun owner(): UUID<Principal>?

    suspend fun roles(): List<UUID<Role>>

    suspend fun login(name: String, password: String): Session

    suspend fun activateSession(securityCode: String): Session

    suspend fun getSession(): Session?

    suspend fun logout()

}