package `fun`.adaptive.auth.backend

import `fun`.adaptive.auth.api.AuthPrincipalApi
import `fun`.adaptive.auth.app.AuthModule
import `fun`.adaptive.auth.context.ensureHas
import `fun`.adaptive.auth.context.ensurePrincipalOrHas
import `fun`.adaptive.auth.context.ofPrincipal
import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.auth.model.*
import `fun`.adaptive.auth.model.CredentialType.ACTIVATION_KEY
import `fun`.adaptive.auth.model.CredentialType.PASSWORD_RESET_KEY
import `fun`.adaptive.auth.util.BCrypt
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.lib.util.error.requirement
import `fun`.adaptive.service.ServiceProvider
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValue.Companion.asAvValue
import `fun`.adaptive.value.store.AvComputeContext
import kotlinx.datetime.Clock.System.now

@ServiceProvider
class AuthPrincipalService : AuthPrincipalApi, ServiceImpl<AuthPrincipalService>() {

    val valueWorker by workerImpl<AvValueWorker>()
    val authWorker by workerImpl<AuthWorker>()

    val securityOfficer
        get() = authWorker.securityOfficer

    val policy
        get() = SecurityPolicy()

    override suspend fun all(): List<AuthPrincipal> {
        ensureHas(securityOfficer)

        return valueWorker.queryByMarker(AuthMarkers.PRINCIPAL).map {
            it.asAvValue<PrincipalSpec>()
        }
    }

    override suspend fun addPrincipal(
        name: String,
        spec: PrincipalSpec,
        activationKey: String?
    ) {
        ensureHas(securityOfficer)

        addPrincipal(name, spec, ACTIVATION_KEY, activationKey)
    }

    internal suspend fun addPrincipal(
        name: String,
        spec: PrincipalSpec,
        credentialType: String,
        credentialSecret: String?,
        account: AvValue<*>? = null
    ): AvValueId {

        val credentialListId = uuid7<AvValue<*>>()

        val refs = mutableMapOf(
            AuthMarkers.CREDENTIAL_LIST to credentialListId
        )

        if (account != null) {
            refs[AuthRefLabels.ACCOUNT_REF] = account.uuid
        }

        val principalValue = AvValue(
            name = name,
            friendlyId = name,
            markersOrNull = setOf(AuthMarkers.PRINCIPAL),
            refsOrNull = refs,
            spec = spec
        )

        val credentials = mutableSetOf<Credential>()

        if (credentialSecret != null) {
            credentials += Credential(
                credentialType,
                BCrypt.hashpw(credentialSecret, BCrypt.gensalt()),
                now()
            )
        }

        val credentialListValue = AvValue(
            uuid = credentialListId,
            markersOrNull = setOf(AuthMarkers.CREDENTIAL_LIST),
            spec = credentials
        )

        valueWorker.execute {
            val uniqueName = valueWorker.queryByMarker(AuthMarkers.PRINCIPAL).none { it.name == name }
            requirement(AuthModule.ALREADY_EXISTS) { uniqueName }

            this += principalValue
            this += credentialListValue

            if (account != null) {
                this += account.copy(refsOrNull = mapOf(AuthRefLabels.PRINCIPAL_REF to principalValue.uuid))
            }
        }

        return principalValue.uuid
    }

    override suspend fun addCredential(
        principalId: AuthPrincipalId,
        credential: Credential,
        currentCredential: Credential?
    ) {
        ensurePrincipalOrHas(principalId, securityOfficer)

        // history(credential.principal)

        if (serviceContext.ofPrincipal(principalId)) {
            requireNotNull(currentCredential)
            getSessionService().authenticate(principalId, currentCredential.value, true, currentCredential.type, policy)
        }

        valueWorker.execute {
            updateCredentials(principalId) {
                it.removeAll { it.type == credential.type }
                it.add(credential.hash())
                it.toSet()
            }
        }
    }

    override suspend fun getOrNull(principalId: AuthPrincipalId): AuthPrincipal? {
        ensurePrincipalOrHas(principalId, securityOfficer)

        return valueWorker.getOrNull<PrincipalSpec>(principalId)
    }

    override suspend fun activate(principalId: AuthPrincipalId, credential: Credential, key: Credential) {
        publicAccess()

        getSessionService().authenticate(principalId, key.value, true, ACTIVATION_KEY, policy)

        // history(credential.principal)

        valueWorker.execute {
            updateCredentials(principalId) {
                it.removeAll { it.type == ACTIVATION_KEY }
                it.add(credential.hash())
                it.toSet()
            }
        }

        updateSpec(principalId) { it.copy(activated = true) }
    }

    override suspend fun resetPassword(
        principalId: AuthPrincipalId,
        credential: Credential,
        key: Credential
    ) {
        publicAccess()

        getSessionService().authenticate(principalId, key.value, true, PASSWORD_RESET_KEY, policy)

        // history(credential.principal)

        valueWorker.execute {
            updateCredentials(principalId) {
                it.removeAll { it.type == PASSWORD_RESET_KEY }
                it.add(credential.hash())
                it.toSet()
            }
        }
    }

    override suspend fun setActivated(
        principalId: AuthPrincipalId,
        activated: Boolean
    ) {
        ensureHas(securityOfficer)

        // history(principalId)

        updateSpec(principalId) { it.copy(activated = activated) }
    }

    override suspend fun setLocked(
        principalId: AuthPrincipalId,
        locked: Boolean
    ) {
        ensureHas(securityOfficer)

        //history(principalId)

        updateSpec(principalId) { it.copy(locked = locked) }
    }

    fun AvComputeContext.updateCredentials(
        principalId: AvValueId,
        updateFun: (MutableSet<Credential>) -> Set<Credential>
    ) {
        val credentialList = ref<Set<Credential>>(principalId, AuthMarkers.CREDENTIAL_LIST)
        this += credentialList.copy(spec = updateFun(credentialList.spec.toMutableSet()))
    }

    private suspend fun updateSpec(
        principalId: AvValueId,
        updateFun: (PrincipalSpec) -> PrincipalSpec
    ) {
        valueWorker.update<PrincipalSpec>(principalId) {
            it.copy(spec = updateFun(it.spec))
        }
    }

    fun getSessionService() =
        safeAdapter.firstImpl<AuthSessionService>().newInstance(Session.contextFor(roleId = securityOfficer))

}