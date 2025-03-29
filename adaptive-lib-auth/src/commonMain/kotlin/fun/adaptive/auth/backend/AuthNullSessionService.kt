package `fun`.adaptive.auth.backend

import `fun`.adaptive.auth.api.AuthSessionApi
import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.auth.model.*
import `fun`.adaptive.auth.model.CredentialType.ACTIVATION_KEY
import `fun`.adaptive.auth.util.BCrypt
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.item.AvItem.Companion.asAvItem
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock.System.now

class AuthNullSessionService : AuthSessionApi, ServiceImpl<AuthNullSessionService> {

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