package `fun`.adaptive.ktor.worker

import `fun`.adaptive.auth.context.ensureLoggedIn
import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.backend.builtin.ServiceImpl

class AuthTestService : AuthTestApi, ServiceImpl<AuthTestService> {

    override suspend fun publicFun(): String {
        publicAccess()
        return "publicFun"
    }

    override suspend fun loggedInFun(): String {
        ensureLoggedIn()
        return "loggedInFun"
    }

}