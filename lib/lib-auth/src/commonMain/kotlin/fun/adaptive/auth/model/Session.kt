/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.model.ServiceSession
import `fun`.adaptive.service.transport.NullTransport
import `fun`.adaptive.utility.CleanupHandler
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.value.AvValueId
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant

@Adat
class Session(
    override val uuid: UUID<Session>,
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

    companion object {
        /**
         * Create an empty session for with [roleId]. Useful for intra-server calls.
         */
        fun forRole(principalId: AvValueId?, roleId: AvValueId) =
            Session(uuid7(), principalId, "", now(), 0L, 0L, setOf(roleId))

        /**
         * Get a service context with an empty session with [roleId]. Useful for intra-server calls.
         */
        fun contextFor(principalId: AvValueId? = null, roleId: AvValueId) =
            ServiceContext(NullTransport, sessionOrNull = forRole(principalId, roleId))
    }

}