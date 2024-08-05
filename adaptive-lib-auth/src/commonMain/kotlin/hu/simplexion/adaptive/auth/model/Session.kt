/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.auth.model

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatEntity
import hu.simplexion.adaptive.service.model.ServiceSession
import hu.simplexion.adaptive.utility.CleanupHandler
import hu.simplexion.adaptive.utility.UUID
import kotlinx.datetime.Instant

@Adat
class Session(
    override val id: UUID<Session>,
    override val principalOrNull: UUID<Principal>?,
    val securityCode: String,
    val createdAt: Instant,
    val vmCreatedAt: Long,
    var lastActivity: Long, // TODO does the synchronization of ConcurrentHashMap enough for lastActivity?
    val roles: List<Role>
) : AdatEntity<Session>, ServiceSession {

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