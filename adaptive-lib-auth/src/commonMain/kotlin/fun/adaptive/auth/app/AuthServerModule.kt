package `fun`.adaptive.auth.app

import `fun`.adaptive.auth.backend.*
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker

class AuthServerModule<AT : Any> : AuthModule<Unit, AT>() {

    override fun BackendAdapter.init() {
        service { AuthPrincipalService() }
        service { AuthRoleService() }
        service { AuthSessionService() }

        worker { AuthSessionWorker() }
        worker { AuthWorker() }
    }

}