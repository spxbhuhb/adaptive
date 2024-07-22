/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.auth.worker

import hu.simplexion.adaptive.auth.model.SecurityPolicy
import hu.simplexion.adaptive.auth.model.Session
import hu.simplexion.adaptive.lib.auth.store.history
import hu.simplexion.adaptive.server.builtin.WorkerImpl
import hu.simplexion.adaptive.server.setting.dsl.setting
import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.utility.vmNowSecond
import kotlinx.coroutines.delay
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.concurrent.ConcurrentHashMap

class SessionWorker : WorkerImpl<SessionWorker> {

    val sessionCookieName by setting<String> { "SESSION_COOKIE_NAME" } default "ADAPTIVE_SESSION"

    /**
     * Sessions waiting for the second step of 2FA.
     */
    internal val preparedSessions = ConcurrentHashMap<UUID<ServiceContext>, Session>()

    /**
     * Active sessions used for authorization. Use the [getSessionForContext] method
     * to get the session.
     */
    internal val activeSessions = ConcurrentHashMap<UUID<ServiceContext>, Session>()

    /**
     * Function to send the security code.
     */
    var sendSecurityCode: (session: Session) -> Unit = { }

    override suspend fun run() {
        while (isActive) {
            val policy = SecurityPolicy()
            cleanActiveSessions(policy)
            cleanPreparedSessions(policy)
            delay(60_000)
        }
    }

    fun cleanActiveSessions(policy: SecurityPolicy) {
        val now = vmNowSecond()
        val cutoff = now - policy.sessionExpirationInterval.inWholeSeconds

        val expired = activeSessions.values.filter { it.lastActivity < cutoff }

        for (session in expired) {

            activeSessions.remove(session.id.cast())

            session.principal?.let {
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

    fun getSessionForContext(sessionUuid: UUID<ServiceContext>): Session? =
        activeSessions.computeIfPresent(sessionUuid) { _, session ->
            session.lastActivity = vmNowSecond()
            session
        } ?: preparedSessions.computeIfPresent(sessionUuid) { _, session ->
            session.lastActivity = vmNowSecond()
            session
        }

}