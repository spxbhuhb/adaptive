/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.lib.auth.worker

import `fun`.adaptive.auth.model.SecurityPolicy
import `fun`.adaptive.auth.model.Session
import `fun`.adaptive.lib.auth.store.history
import `fun`.adaptive.server.builtin.WorkerImpl
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.model.ServiceSession
import `fun`.adaptive.service.transport.ServiceSessionProvider
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.vmNowSecond
import kotlinx.coroutines.delay
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.concurrent.CancellationException
import java.util.concurrent.ConcurrentHashMap

class SessionWorker : WorkerImpl<SessionWorker>, ServiceSessionProvider {

    /**
     * Sessions waiting for the second step of 2FA.
     */
    internal val preparedSessions = ConcurrentHashMap<UUID<ServiceContext>, Session>()

    /**
     * Active sessions used for authorization. Use the [getSession] method
     * to get the session.
     */
    internal val activeSessions = ConcurrentHashMap<UUID<ServiceContext>, Session>()

    /**
     * Function to send the security code.
     */
    var sendSecurityCode: (session: Session) -> Unit = { }

    override suspend fun run() {
        try {
            while (isActive) {
                val policy = SecurityPolicy()
                cleanActiveSessions(policy)
                cleanPreparedSessions(policy)
                delay(60_000)
            }
        } catch (ex: CancellationException) {
            // delay throws cancellation exception
        }
    }

    fun cleanActiveSessions(policy: SecurityPolicy) {
        val now = vmNowSecond()
        val cutoff = now - policy.sessionExpirationInterval.inWholeSeconds

        val expired = activeSessions.values.filter { it.lastActivity < cutoff }

        for (session in expired) {

            activeSessions.remove(session.id.cast())

            session.principalOrNull?.let {
                transaction {
                    history(it, session.id)
                }
            }
        }
    }

    fun cleanPreparedSessions(policy: SecurityPolicy) {
        val now = vmNowSecond()
        val preparedCutoff = now - policy.sessionActivationInterval.inWholeSeconds

        val expired = preparedSessions.values.filter { it.vmCreatedAt < preparedCutoff }

        for (session in expired) {
            preparedSessions.remove(session.id.cast())
        }
    }

    override fun getSession(uuid: UUID<ServiceContext>): ServiceSession? =
        activeSessions.computeIfPresent(uuid) { _, session ->
            session.lastActivity = vmNowSecond()
            session
        } ?: preparedSessions.computeIfPresent(uuid) { _, session ->
            session.lastActivity = vmNowSecond()
            session
        }

}