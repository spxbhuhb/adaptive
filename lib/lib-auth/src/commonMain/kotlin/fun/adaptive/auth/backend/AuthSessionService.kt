package `fun`.adaptive.auth.backend

import `fun`.adaptive.auth.api.AuthSessionApi
import `fun`.adaptive.service.auth.ensuredByLogic
import `fun`.adaptive.auth.context.getPrincipalIdOrNull
import `fun`.adaptive.auth.context.getSessionOrNull
import `fun`.adaptive.service.auth.publicAccess
import `fun`.adaptive.auth.model.*
import `fun`.adaptive.auth.model.CredentialType.ACTIVATION_KEY
import `fun`.adaptive.auth.util.BCrypt
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.service.ServiceProvider
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.secureRandom
import `fun`.adaptive.utility.use
import `fun`.adaptive.utility.vmNowSecond
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.AvValue.Companion.asAvValue
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.time.Clock.System.now

@ServiceProvider
class AuthSessionService : AuthSessionApi, ServiceImpl<AuthSessionService>() {

    val valueWorker by workerImpl<AvValueWorker>()
    val sessionWorker by workerImpl<AuthSessionWorker>()

    // ----------------------------------------------------------------------------------
    // API functions
    // ----------------------------------------------------------------------------------

    override suspend fun owner(): AuthPrincipalId? {
        ensuredByLogic("Session owner gets own principal.")
        return serviceContext.getPrincipalIdOrNull()
    }

    override suspend fun roles(): Set<AuthRoleId> {
        ensuredByLogic("Session owner gets own roles.")
        return serviceContext.getSessionOrNull()?.roles ?: emptySet()
    }

    override suspend fun signIn(name: String, password: String): Session {
        publicAccess()

        val policy = getPolicy()
        val principal = getPrincipalByName(name) ?: throw AuthenticationFail(AuthenticationResult.UnknownPrincipal)

        // history is written by authenticate

        authenticate(principal.uuid, password, true, CredentialType.PASSWORD, policy)

        val vmNow = vmNowSecond()

        val session = Session(
            uuid = serviceContext.uuid.cast(),
            principalOrNull = principal.uuid,
            securityCode = abs(secureRandom(1)[0]).toString().padStart(6, '0').substring(0, 6),
            createdAt = now(),
            vmCreatedAt = vmNow,
            lastActivity = vmNow,
            roles = principal.spec.roles
        )

        if (getPolicy().twoFactorAuthentication) {
            sessionWorker.addPreparedSession(session)
            sessionWorker.sendSecurityCode(session)
        } else {
            sessionWorker.addActiveSession(session)
            serviceContext.disconnect = true
        }

        return session
    }

    override suspend fun activateSession(securityCode: String): Session {
        publicAccess()

        val session = sessionWorker.getPreparedSession(serviceContext.uuid)
            ?: throw AuthenticationFail(AuthenticationResult.UnknownSession)

        if (session.securityCode != securityCode) {
            // maybe the user opened way too many windows
            if (
                sessionWorker.hasPreparedSession(session.principalOrNull, securityCode)
            ) {
                // FIXME do we want security code brute force detection?
                throw AuthenticationFail(AuthenticationResult.InvalidSecurityCode)
            }
        }

        // history(serviceContext.getPrincipalId(), AuthenticationResult.Success)

        sessionWorker.removePreparedSession(serviceContext.uuid)
        sessionWorker.addActiveSession(session)
        serviceContext.disconnect = true

        return session
    }

    override suspend fun getSession(): Session? {
        ensuredByLogic("Session owner gets own session.")

        return serviceContext.getSessionOrNull()
    }

    override suspend fun signOut() {
        publicAccess()

        serviceContext.disconnect = true

        if (serviceContext.getSessionOrNull() == null) return

        // history(serviceContext.getPrincipalId())

        sessionWorker.removeActiveSession(serviceContext.uuid)

        return
    }

    // ----------------------------------------------------------------------------------
    // Non-API functions
    // ----------------------------------------------------------------------------------

    private val authenticateLock = getLock()
    private val authenticateInProgress = mutableSetOf<AuthPrincipalId>()

    suspend fun authenticate(
        principalId: AuthPrincipalId,
        password: String,
        checkCredentials: Boolean,
        credentialType: String,
        policy: SecurityPolicy
    ) {

        // FIXME check credential expiration
        val principal = valueWorker.get<PrincipalSpec>(principalId)

        val validCredentials = if (checkCredentials) {
            val credentialList = valueWorker.ref<CredentialSet>(principal.uuid, AuthMarkers.CREDENTIAL_LIST)

            val credential = credentialList.spec.firstOrNull { it.type == credentialType }
                ?: throw AuthenticationFail(AuthenticationResult.NoCredential)

            BCrypt.checkpw(password, credential.value)
        } else {
            true
        }

        // this is here to prevent SQL deadlocks
        lockState(principalId)

        try {
            val spec = principal.spec

            val result = when {
                ! spec.activated && credentialType != ACTIVATION_KEY -> AuthenticationResult.NotActivated
                spec.locked -> AuthenticationResult.Locked
                spec.expired -> AuthenticationResult.Expired
                spec.anonymized -> AuthenticationResult.Anonymized
                ! validCredentials -> AuthenticationResult.InvalidCredentials
                else -> null
            }

            if (result != null) {
                updateSpec(principalId) {
                    it.copy(
                        authFailCount = it.authFailCount + 1,
                        lastAuthFail = now(),
                        locked = it.locked || (it.authFailCount > policy.maxFailedAuths)
                    )
                }

                // history(principal.id, result)

                throw AuthenticationFail(result)
            }

            updateSpec(principalId) {
                it.copy(
                    lastAuthSuccess = now(),
                    authSuccessCount = it.authSuccessCount + 1,
                    authFailCount = 0,
                )
            }

            // history(principal.id, AuthenticationResult.Success)

        } finally {
            releaseState(principalId)
        }
    }

    private suspend fun lockState(principalId: AuthPrincipalId) {
        var success = false
        var tryNumber = 5
        while (tryNumber > 0 && ! success) {
            success = authenticateLock.use {
                if (principalId in authenticateInProgress) {
                    delay(100)
                    false
                } else {
                    authenticateInProgress += principalId
                    true
                }
            }
            tryNumber --
        }
        if (! success) throw RuntimeException("couldn't lock principal state in 5 tries")
    }

    private fun releaseState(principalId: AuthPrincipalId) {
        authenticateLock.use {
            authenticateInProgress -= principalId
        }
    }

    fun getPolicy(): SecurityPolicy {
        return SecurityPolicy()
    }

    private fun getPrincipalByName(name: String): AuthPrincipal? {
        return valueWorker.queryByMarker(AuthMarkers.PRINCIPAL).firstOrNull { it.name == name }?.asAvValue()
    }

    private suspend fun updateSpec(
        principalId: AvValueId,
        updateFun: (PrincipalSpec) -> PrincipalSpec
    ) {
        valueWorker.update<PrincipalSpec>(principalId) {
            it.copy(spec = updateFun(it.spec))
        }
    }

}