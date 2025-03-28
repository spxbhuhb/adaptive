package `fun`.adaptive.auth.backend

import `fun`.adaptive.auth.api.AuthPrincipalApi
import `fun`.adaptive.auth.context.ensureOneOf
import `fun`.adaptive.auth.context.ensurePrincipalOrOneOf
import `fun`.adaptive.auth.context.ofPrincipal
import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.auth.model.*
import `fun`.adaptive.auth.model.CredentialType.ACTIVATION_KEY
import `fun`.adaptive.auth.model.CredentialType.PASSWORD
import `fun`.adaptive.auth.model.CredentialType.PASSWORD_RESET_KEY
import `fun`.adaptive.auth.util.BCrypt
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvItem.Companion.asAvItem
import kotlinx.datetime.Clock.System.now

class AuthPrincipalService : AuthPrincipalApi, ServiceImpl<AuthPrincipalService> {

    companion object {
        val lock = getLock()

        var addRoles: Array<AvValueId> = emptyArray<AvValueId>()
            get() = lock.use { field }
            set(value) = lock.use { field = value }

        var getRoles: Array<AvValueId> = emptyArray<AvValueId>()
            get() = lock.use { field }
            set(value) = lock.use { field = value }

        var updateRoles: Array<AvValueId> = emptyArray<AvValueId>()
            get() = lock.use { field }
            set(value) = lock.use { field = value }

        lateinit var worker: AvValueWorker
    }

    override fun mount() {
        check(GlobalRuntimeContext.isServer)
        worker = safeAdapter.firstImpl<AvValueWorker>()
    }

    val sessionService
        get() = AuthSessionService().newInstance(serviceContext)

    val policy
        get() = SecurityPolicy()

    override suspend fun all(): List<AuthPrincipal> =
        ensureOneOf(*getRoles).let {
            worker.queryByMarker(AuthMarkers.PRINCIPAL).map {
                it.asAvItem<PrincipalSpec>()
            }
        }

    override suspend fun addPrincipal(
        name: String,
        spec: PrincipalSpec,
        activationKey: String?
    ) {
        ensureOneOf(*addRoles)

        val credentialListId = uuid7<AvValue>()

        val principalValue = AvItem(
            name = name,
            type = AUTH_PRINCIPAL,
            parentId = null,
            markersOrNull = mapOf(AuthMarkers.CREDENTIAL_LIST to credentialListId),
            friendlyId = name,
            specific = spec
        )

        val credentials = mutableSetOf<Credential>()

        if (activationKey != null) {
            credentials += Credential(
                ACTIVATION_KEY,
                BCrypt.hashpw(activationKey, BCrypt.gensalt()),
                now()
            )
        }

        val credentialListValue = AuthCredentialList(
            uuid = credentialListId,
            parentId = principalValue.uuid,
            type = AUTH_CREDENTIAL_LIST,
            credentials = credentials
        )

        //history(principal.id)

        worker.execute {
            val uniqueName = worker.queryByMarker(AuthMarkers.PRINCIPAL).none { it.name == name }
            require(uniqueName) { "principal with the same name already exists" }

            this += principalValue
            this += credentialListValue
        }

    }

    override suspend fun addCredential(
        principalId: AuthPrincipalId,
        credential: Credential,
        currentCredential: Credential?
    ) {

        ensurePrincipalOrOneOf(principalId, updateRoles)

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

    override suspend fun getOrNull(principalId: AuthPrincipalId): AuthPrincipal? =

        ensurePrincipalOrOneOf(principalId, getRoles).let {
            worker[principalId.cast()]?.asAvItem<PrincipalSpec>()
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
        ensureOneOf(*updateRoles)

        // history(principalId)

        updateSpec(principalId) { it.copy(activated = activated) }
    }

    override suspend fun setLocked(
        principalId: AuthPrincipalId,
        locked: Boolean
    ) {
        ensureOneOf(*updateRoles)

        //history(principalId)

        updateSpec(principalId) { it.copy(locked = locked) }
    }

    private fun Credential.hash(type: String? = null) =
        Credential(type ?: PASSWORD, BCrypt.hashpw(value, BCrypt.gensalt()), now())

    private fun AvValueWorker.WorkerComputeContext.updateCredentials(
        principalId: AvValueId,
        updateFun: (MutableSet<Credential>) -> Set<Credential>
    ) {
        val credentialList = markerVal<AuthCredentialList>(principalId.cast(), AUTH_CREDENTIAL_LIST)
        this += credentialList.copy(timestamp = now(), credentials = updateFun(credentialList.credentials.toMutableSet()))
    }

    private suspend fun updateSpec(
        principalId: AvValueId,
        updateFun: (PrincipalSpec) -> PrincipalSpec
    ) {
        worker.update<AvValue>(principalId) { item ->
            item.asAvItem<PrincipalSpec>().let { it.copy(specific = updateFun(it.specific !!)) }
        }
    }
}