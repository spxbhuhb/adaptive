package `fun`.adaptive.lib.auth.service

import `fun`.adaptive.auth.api.SessionApi
import `fun`.adaptive.auth.context.*
import `fun`.adaptive.auth.model.*
import `fun`.adaptive.auth.model.CredentialType.ACTIVATION_KEY
import `fun`.adaptive.lib.auth.crypto.BCrypt
import `fun`.adaptive.lib.auth.store.credentials
import `fun`.adaptive.lib.auth.store.history
import `fun`.adaptive.lib.auth.store.principals
import `fun`.adaptive.lib.auth.store.roleGrants
import `fun`.adaptive.lib.auth.worker.SessionWorker
import `fun`.adaptive.server.builtin.ServiceImpl
import `fun`.adaptive.server.builtin.worker
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.fourRandomInt
import `fun`.adaptive.utility.vmNowSecond
import kotlinx.datetime.Clock.System.now
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.math.abs

class SessionService : SessionApi, ServiceImpl<SessionService> {

    val worker by worker<SessionWorker>()

    // ----------------------------------------------------------------------------------
    // API functions
    // ----------------------------------------------------------------------------------

    override suspend fun owner(): UUID<Principal>? {
        ensuredByLogic("Session owner gets own principal.")
        return serviceContext.getPrincipalIdOrNull()
    }

    override suspend fun roles(): List<UUID<Role>> {
        ensuredByLogic("Session owner gets own roles.")
        return serviceContext.getSessionOrNull()?.roles?.map { it.id } ?: emptyList()
    }

    override suspend fun login(name: String, password: String): Session {
        publicAccess()

        val policy = getPolicy()
        val principal = principals.getByNameOrNull(name) ?: throw AuthenticationFail(AuthenticationResult.UnknownPrincipal)

        // history is written by authenticate

        authenticate(principal.id, password, true, CredentialType.PASSWORD, policy)

        val vmNow = vmNowSecond()

        val session = Session(
            id = serviceContext.uuid.cast(),
            principalOrNull = principal.id,
            securityCode = abs(fourRandomInt()[0]).toString().padStart(6, '0').substring(0, 6),
            createdAt = now(),
            vmCreatedAt = vmNow,
            lastActivity = vmNow,
            roles = roleGrants.rolesOf(principal.id, null)
        )

        if (getPolicy().twoFactorAuthentication) {
            worker.preparedSessions[serviceContext.uuid] = session
            worker.sendSecurityCode(session)
        } else {
            worker.activeSessions[serviceContext.uuid] = session
            serviceContext.disconnect = true
        }

        return session
    }

    override suspend fun activateSession(securityCode: String): Session {
        publicAccess()

        val session = worker.preparedSessions[serviceContext.uuid]
            ?: throw AuthenticationFail(AuthenticationResult.UnknownSession)

        if (session.securityCode != securityCode) {
            // maybe the user opened way too many windows
            if (
                worker.preparedSessions.values.none {
                    it.principalOrNull == session.principalOrNull && it.securityCode == securityCode
                }
            ) {
                // FIXME do we want security code brute force detection?
                throw AuthenticationFail(AuthenticationResult.InvalidSecurityCode)
            }
        }

        history(serviceContext.getPrincipalId(), AuthenticationResult.Success)

        worker.preparedSessions.remove(serviceContext.uuid)
        worker.activeSessions[serviceContext.uuid] = session
        serviceContext.disconnect = true

        return session
    }

    override suspend fun getSession(): Session? {
        ensuredByLogic("Session owner gets own session.")

        return serviceContext.getSessionOrNull()
    }

    override suspend fun logout() {
        publicAccess()

        serviceContext.disconnect = true

        if (serviceContext.getSessionOrNull() == null) return

        history(serviceContext.getPrincipalId())

        worker.activeSessions.remove(serviceContext.uuid)

        TransactionManager.current().commit()

        return
    }

    // ----------------------------------------------------------------------------------
    // Non-API functions
    // ----------------------------------------------------------------------------------

    private val authenticateLock = ReentrantLock()
    private val authenticateInProgress = mutableSetOf<UUID<Principal>>()

    fun authenticate(
        principalId: UUID<Principal>,
        password: String,
        checkCredentials: Boolean,
        credentialType: String,
        policy: SecurityPolicy
    ) {

        // FIXME check credential expiration

        val validCredentials = if (checkCredentials) {
            val credential = credentials.readValue(principalId, credentialType)
                ?: throw AuthenticationFail(AuthenticationResult.NoCredential)

            BCrypt.checkpw(password, credential)
        } else {
            true
        }

        // this is here to prevent SQL deadlocks
        lockState(principalId)

        try {
            val principal = principals[principalId]

            val result = when {
                ! principal.activated && credentialType != ACTIVATION_KEY -> AuthenticationResult.NotActivated
                principal.locked -> AuthenticationResult.Locked
                principal.expired -> AuthenticationResult.Expired
                principal.anonymized -> AuthenticationResult.Anonymized
                ! validCredentials -> AuthenticationResult.InvalidCredentials
                else -> null
            }

            if (result != null) {
                principal.authFailCount ++
                principal.lastAuthFail = now()
                principal.locked = principal.locked || (principal.authFailCount > policy.maxFailedAuths)

                principals %= principal

                history(principal.id, result)

                TransactionManager.current().commit()

                throw AuthenticationFail(result)
            }

            principal.lastAuthSuccess = now()
            principal.authSuccessCount ++
            principal.authFailCount = 0

            principals %= principal

            history(principal.id, AuthenticationResult.Success)

            TransactionManager.current().commit()

        } finally {
            releaseState(principalId)
        }
    }

    private fun lockState(principalId: UUID<Principal>) {
        var success = false
        for (tryNumber in 1 .. 5) {
            success = authenticateLock.withLock {
                if (principalId in authenticateInProgress) {
                    Thread.sleep(100)
                    false
                } else {
                    authenticateInProgress += principalId
                    true
                }
            }
            if (success) break
        }
        if (! success) throw RuntimeException("couldn't lock principal state in 5 tries")
    }

    private fun releaseState(principalId: UUID<Principal>) {
        authenticateLock.withLock {
            authenticateInProgress -= principalId
        }
    }

    fun getPolicy(): SecurityPolicy {
        return SecurityPolicy()
    }

}