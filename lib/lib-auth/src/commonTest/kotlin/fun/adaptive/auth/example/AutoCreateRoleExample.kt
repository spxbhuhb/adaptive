package `fun`.adaptive.auth.example

import `fun`.adaptive.auth.backend.AuthWorker
import `fun`.adaptive.auth.model.RoleSpec
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.utility.UUID.Companion.uuid4

/**
 * # Create a role during application bootstrap
 *
 * Call [getOrCreateRole](function://) with the role name and the role-specific data.
 *
 * The [uuid](parameter://) parameter is optional, if not specified a random UUID will be generated.
 */
class AutoCreateRoleExample : WorkerImpl<AutoCreateRoleExample>() {

    val authWorker by workerImpl<AuthWorker>()

    override suspend fun run() {
        authWorker.getOrCreateRole(name = "programmatic-role-name", spec = RoleSpec(), uuid = uuid4())
    }

}