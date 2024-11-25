package `fun`.adaptive.lib.auth.service

import `fun`.adaptive.adat.ensureValid
import `fun`.adaptive.auth.api.PrincipalApi
import `fun`.adaptive.auth.context.ensureOneOf
import `fun`.adaptive.auth.context.ensurePrincipalOrOneOf
import `fun`.adaptive.auth.context.ofPrincipal
import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.auth.model.Credential
import `fun`.adaptive.auth.model.CredentialType.ACTIVATION_KEY
import `fun`.adaptive.auth.model.CredentialType.PASSWORD
import `fun`.adaptive.auth.model.CredentialType.PASSWORD_RESET_KEY
import `fun`.adaptive.auth.model.Principal
import `fun`.adaptive.auth.model.Role
import `fun`.adaptive.auth.model.SecurityPolicy
import `fun`.adaptive.lib.auth.crypto.BCrypt
import `fun`.adaptive.lib.auth.store.credentials
import `fun`.adaptive.lib.auth.store.history
import `fun`.adaptive.lib.auth.store.principals
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import kotlinx.datetime.Clock.System.now

class PrincipalService : PrincipalApi, ServiceImpl<PrincipalService> {

    companion object {
        var addRoles: Array<UUID<Role>> = emptyArray<UUID<Role>>()
            get() = synchronized(field) { field }
            set(value) = synchronized(field) { field = value }

        var getRoles: Array<UUID<Role>> = emptyArray<UUID<Role>>()
            get() = synchronized(field) { field }
            set(value) = synchronized(field) { field = value }

        var updateRoles: Array<UUID<Role>> = emptyArray<UUID<Role>>()
            get() = synchronized(field) { field }
            set(value) = synchronized(field) { field = value }
    }

    val sessionService
        get() = SessionService().newInstance(serviceContext)

    val policy
        get() = SecurityPolicy()

    override suspend fun all(): List<Principal> =

        ensureOneOf(*getRoles).let {
            principals.all()
        }

    override suspend fun addPrincipal(
        principal: Principal,
        activated: Boolean,
        activationKey: String?,
    ) {

        ensureOneOf(*addRoles)
        ensureValid(principal)

        history(principal.id)

        principals += principal

        if (activationKey != null) {
            credentials += Credential(
                UUID(),
                principal.id,
                ACTIVATION_KEY,
                BCrypt.hashpw(activationKey, BCrypt.gensalt()),
                now()
            )
        }
    }

    override suspend fun addCredential(credential: Credential, currentCredential: Credential?) {

        ensurePrincipalOrOneOf(credential.principal, updateRoles)

        history(credential.principal)

        if (serviceContext.ofPrincipal(credential.principal)) {
            requireNotNull(currentCredential)
            sessionService.authenticate(credential.principal, currentCredential.value, true, currentCredential.type, policy)
        }

        credential.createdAt = now()
        credential.value = BCrypt.hashpw(credential.value, BCrypt.gensalt())

        credentials += credential
    }

    override suspend fun get(principalId: UUID<Principal>): Principal =

        ensurePrincipalOrOneOf(principalId, getRoles).let {
            principals[principalId]
        }

    override suspend fun activate(credential: Credential, key: Credential) {
        publicAccess()

        sessionService.authenticate(key.principal, key.value, true, ACTIVATION_KEY, policy)

        credentials.removeActivationKeys(key.principal)

        credentials += Credential(
            UUID(),
            key.principal,
            PASSWORD,
            BCrypt.hashpw(credential.value, BCrypt.gensalt()),
            now()
        )

        history(credential.principal)

        principals.setActivated(key.principal, true)
    }

    override suspend fun resetPassword(credential: Credential, key: Credential) {
        publicAccess()

        sessionService.authenticate(key.principal, key.value, true, PASSWORD_RESET_KEY, policy)

        history(credential.principal)

        credentials.removePasswordResetKeys(key.principal)

        credentials += Credential(
            UUID(),
            key.principal,
            PASSWORD,
            BCrypt.hashpw(credential.value, BCrypt.gensalt()),
            now()
        )
    }

    override suspend fun setActivated(principalId: UUID<Principal>, activated: Boolean) {
        ensureOneOf(*updateRoles)

        history(principalId)

        principals.setActivated(principalId, activated)
    }

    override suspend fun setLocked(principalId: UUID<Principal>, locked: Boolean) {
        ensureOneOf(*updateRoles)

        history(principalId)

        principals.setLocked(principalId, locked)
    }

}