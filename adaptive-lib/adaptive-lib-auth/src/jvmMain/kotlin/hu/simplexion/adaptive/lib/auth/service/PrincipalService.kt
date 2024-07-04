package hu.simplexion.adaptive.lib.auth.service

import hu.simplexion.adaptive.adat.ensureValid
import hu.simplexion.adaptive.auth.api.PrincipalApi
import hu.simplexion.adaptive.auth.model.Credential
import hu.simplexion.adaptive.auth.model.CredentialType.ACTIVATION_KEY
import hu.simplexion.adaptive.auth.model.CredentialType.PASSWORD
import hu.simplexion.adaptive.auth.model.CredentialType.PASSWORD_RESET_KEY
import hu.simplexion.adaptive.auth.model.Principal
import hu.simplexion.adaptive.auth.model.Role
import hu.simplexion.adaptive.auth.model.SecurityPolicy
import hu.simplexion.adaptive.lib.auth.context.ensureOneOf
import hu.simplexion.adaptive.lib.auth.context.ensurePrincipalOrOneOf
import hu.simplexion.adaptive.lib.auth.context.isPrincipal
import hu.simplexion.adaptive.lib.auth.context.publicAccess
import hu.simplexion.adaptive.lib.auth.crypto.BCrypt
import hu.simplexion.adaptive.lib.auth.store.credentials
import hu.simplexion.adaptive.lib.auth.store.history
import hu.simplexion.adaptive.lib.auth.store.principals
import hu.simplexion.adaptive.server.builtin.ServiceImpl
import hu.simplexion.adaptive.utility.UUID
import kotlinx.datetime.Clock.System.now

class PrincipalService : PrincipalApi, ServiceImpl<PrincipalService> {

    companion object {
        var addRoles = emptyArray<UUID<Role>>()
        var getRoles = emptyArray<UUID<Role>>()
        var updateRoles = emptyArray<UUID<Role>>()
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
        activationKey: String?
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

        if (serviceContext.isPrincipal(credential.principal)) {
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