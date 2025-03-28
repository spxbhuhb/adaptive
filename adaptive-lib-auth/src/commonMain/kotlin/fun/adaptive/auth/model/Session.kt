/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatEntity
import `fun`.adaptive.service.model.ServiceSession
import `fun`.adaptive.utility.CleanupHandler
import `fun`.adaptive.utility.UUID
import kotlinx.datetime.Instant

@Adat
class Session(
    override val id: UUID<Session>,
    override val principalOrNull: AuthPrincipalId?,
    val securityCode: String,
    val createdAt: Instant,
    val vmCreatedAt: Long,
    var lastActivity: Long, // TODO does the synchronization of ConcurrentHashMap enough for lastActivity?
    val roles: Set<AuthRoleId>
) : ServiceSession {

    override fun addSessionCleanup(cleanup: CleanupHandler<ServiceSession>) {
        TODO("Not yet implemented")
    }

    override fun removeSessionCleanup(cleanup: CleanupHandler<ServiceSession>) {
        TODO("Not yet implemented")
    }

    override fun cleanup() {
        TODO("Not yet implemented")
    }

}