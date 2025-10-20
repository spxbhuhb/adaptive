package `fun`.adaptive.auth.backend

import `fun`.adaptive.auth.api.AuthSessionApi
import `fun`.adaptive.service.auth.publicAccess
import `fun`.adaptive.auth.model.AuthPrincipalId
import `fun`.adaptive.auth.model.AuthRoleId
import `fun`.adaptive.auth.model.Session
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.service.ServiceProvider

@ServiceProvider
class AuthNullSessionService : AuthSessionApi, ServiceImpl<AuthNullSessionService>() {

    override suspend fun owner(): AuthPrincipalId? {
        publicAccess()
        return null
    }

    override suspend fun roles(): Set<AuthRoleId> {
        publicAccess()
        return emptySet()
    }

    override suspend fun signIn(name: String, password: String): Session {
        publicAccess()
        unsupported()
    }

    override suspend fun activateSession(securityCode: String): Session {
        publicAccess()
        unsupported()
    }

    override suspend fun getSession(): Session? {
        publicAccess()
        return null
    }

    override suspend fun signOut() {
        publicAccess()
        unsupported()
    }

}