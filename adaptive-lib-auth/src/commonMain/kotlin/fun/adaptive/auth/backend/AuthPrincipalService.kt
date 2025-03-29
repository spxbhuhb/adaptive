package `fun`.adaptive.auth.backend

import `fun`.adaptive.auth.api.AuthPrincipalApi
import `fun`.adaptive.auth.backend.AuthWorker.Companion.securityOfficer
import `fun`.adaptive.auth.context.ensureHas
import `fun`.adaptive.auth.context.ensurePrincipalOrHas
import `fun`.adaptive.auth.context.ofPrincipal
import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.auth.model.*
import `fun`.adaptive.auth.model.CredentialType.ACTIVATION_KEY
import `fun`.adaptive.auth.model.CredentialType.PASSWORD
import `fun`.adaptive.auth.model.CredentialType.PASSWORD_RESET_KEY
import `fun`.adaptive.auth.util.BCrypt
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvItem.Companion.asAvItem
import kotlinx.datetime.Clock.System.now

class AuthPrincipalService : AuthPrincipalApi, ServiceImpl<AuthPrincipalService> {

    companion object {
        lateinit var worker: AvValueWorker
    }

    override fun mount() {
        worker = safeAdapter.firstImpl<AvValueWorker>()
    }

    val sessionService
        get() = AuthSessionService().newInstance(serviceContext)

    val policy
        get() = SecurityPolicy()

    override suspend fun all(): List<AuthPrincipal> {
        ensureHas(securityOfficer)

        return worker.queryByMarker(AuthMarkers.PRINCIPAL).map {
            it.asAvItem<PrincipalSpec>()
        }
    }

    override suspend fun addPrincipal(
        name: String,
        spec: PrincipalSpec,
        activationKey: String?
    ) {
        ensureHas(securityOfficer)

        val (principalValue, credentialListValue) = addPrincipalPrep(name, spec, ACTIVATION_KEY, activationKey)

        //history(principal.id)

        worker.execute {
            val uniqueName = worker.queryByMarker(AuthMarkers.PRINCIPAL).none { it.name == name }
            require(uniqueName) { "principal with the same name already exists" }

            this += principalValue
            this += credentialListValue
        }

    }

    internal fun addPrincipalPrep(
        name: String,
        spec: PrincipalSpec,
        credentialType: String,
        credentialSecret: String?
    ): Pair<AvItem<PrincipalSpec>, CredentialList> {

        val credentialListId = uuid7<AvValue>()

        val principalValue = AvItem(
            name = name,
            type = AUTH_PRINCIPAL,
            parentId = null,
            markersOrNull = mapOf(
                AuthMarkers.PRINCIPAL to null,
                AuthMarkers.CREDENTIAL_LIST to credentialListId
            ),
            friendlyId = name,
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

        val credentialListValue = CredentialList(
            uuid = credentialListId,
            parentId = principalValue.uuid,
            type = AUTH_CREDENTIAL_LIST,
            credentials = credentials
        )

        return (principalValue to credentialListValue)
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
            sessionService.authenticate(principalId, currentCredential.value, true, currentCredential.type, policy)
        }

        worker.execute {
            updateCredentials(principalId) {
                it.removeAll { it.type == credential.type }
                it.add(credential.hash())
                it.toSet()
            }
        }
    }

    override suspend fun getOrNull(principalId: AuthPrincipalId): AuthPrincipal? {
        ensurePrincipalOrHas(principalId, securityOfficer)

        return worker[principalId.cast()]?.asAvItem<PrincipalSpec>()
    }

    override suspend fun activate(principalId: AuthPrincipalId, credential: Credential, key: Credential) {
        publicAccess()

        sessionService.authenticate(principalId, key.value, true, ACTIVATION_KEY, policy)

        // history(credential.principal)

        worker.execute {
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

        sessionService.authenticate(principalId, key.value, true, PASSWORD_RESET_KEY, policy)

        // history(credential.principal)

        worker.execute {
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

    private fun Credential.hash(type: String? = null) =
        Credential(type ?: PASSWORD, BCrypt.hashpw(value, BCrypt.gensalt()), now())

    private fun AvValueWorker.WorkerComputeContext.updateCredentials(
        principalId: AvValueId,
        updateFun: (MutableSet<Credential>) -> Set<Credential>
    ) {
        val credentialList = markerVal<CredentialList>(principalId.cast(), AUTH_CREDENTIAL_LIST)
        this += credentialList.copy(timestamp = now(), credentials = updateFun(credentialList.credentials.toMutableSet()))
    }

    private suspend fun updateSpec(
        principalId: AvValueId,
        updateFun: (PrincipalSpec) -> PrincipalSpec
    ) {
        worker.update<AvValue>(principalId) { item ->
            item.asAvItem<PrincipalSpec>().let { it.copy(spec = updateFun(it.spec)) }
        }
    }
}