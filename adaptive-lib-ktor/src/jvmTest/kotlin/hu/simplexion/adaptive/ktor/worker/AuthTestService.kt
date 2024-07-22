package hu.simplexion.adaptive.ktor.worker

import hu.simplexion.adaptive.lib.auth.context.ensureLoggedIn
import hu.simplexion.adaptive.lib.auth.context.publicAccess
import hu.simplexion.adaptive.server.builtin.ServiceImpl

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