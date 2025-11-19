/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.backend

import `fun`.adaptive.auth.model.AuthPrincipal
import `fun`.adaptive.auth.model.AuthPrincipalId
import `fun`.adaptive.auth.model.SecurityPolicy
import `fun`.adaptive.auth.model.Session
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.model.ServiceSession
import `fun`.adaptive.service.transport.ServiceSessionProvider
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import `fun`.adaptive.utility.vmNowMicro
import `fun`.adaptive.utility.vmNowSecond
import kotlinx.coroutines.delay
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Clock

class AuthSessionWorker : WorkerImpl<AuthSessionWorker>(), ServiceSessionProvider {

    val lock = getLock()

    /**
     * Sessions waiting for the second step of 2FA.
     */
    private val preparedSessions = mutableMapOf<UUID<ServiceContext>, Session>()

    /**
     * Active sessions used for authorization. Use the [getSession] method
     * to get the session.
     */
    private val activeSessions = mutableMapOf<UUID<ServiceContext>, Session>()

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

        val expired = lock.use { activeSessions.values.filter { it.lastActivity < cutoff } }

        for (session in expired) {

            lock.use { activeSessions.remove(session.uuid.cast()) }

            session.principalOrNull?.let {
//                    history(it, session.id)
            }
        }
    }

    fun cleanPreparedSessions(policy: SecurityPolicy) {
        val now = vmNowSecond()
        val preparedCutoff = now - policy.sessionActivationInterval.inWholeSeconds

        lock.use {
            val expired = preparedSessions.values.filter { it.vmCreatedAt < preparedCutoff }

            for (session in expired) {
                preparedSessions.remove(session.uuid.cast())
            }
        }
    }

    fun addPreparedSession(session: Session) {
        lock.use {
            preparedSessions[session.uuid.cast()] = session
        }
    }

    fun getPreparedSession(uuid: UUID<ServiceContext>) =
        lock.use {
            preparedSessions[uuid]
        }

    fun hasPreparedSession(principalId: AuthPrincipalId?, securityCode: String) =
        lock.use {
            if (principalId == null) return@use false
            preparedSessions.values.none {
                it.principalOrNull == principalId && it.securityCode == securityCode
            }
        }

    fun removePreparedSession(uuid: UUID<ServiceContext>) =
        lock.use {
            preparedSessions.remove(uuid)
        }

    fun addActiveSession(session: Session) {
        lock.use {
            activeSessions[session.uuid.cast()] = session
        }
    }

    fun removeActiveSession(uuid: UUID<ServiceContext>) =
        lock.use {
            activeSessions.remove(uuid)
        }


    override fun getSession(uuid: UUID<ServiceContext>): ServiceSession? =
        lock.use {
            if (autoLogin) {
                val vmNow = vmNowMicro()

                return Session(
                    uuid.cast(),
                    UUID.nil(),
                    "",
                    Clock.System.now(),
                    vmNow,
                    vmNow,
                    emptySet()
                )
            }

            val activeSession = activeSessions[uuid]
            if (activeSession != null) {
                activeSession.lastActivity = vmNowSecond()
                return activeSession
            }

            val preparedSession = preparedSessions[uuid]
            if (preparedSession != null) {
                preparedSession.lastActivity = vmNowSecond()
                return preparedSession
            }

            return null
        }

    companion object {
        var autoLogin = false
    }
}