package `fun`.adaptive.cookbook

import `fun`.adaptive.auth.model.Credential
import `fun`.adaptive.auth.model.CredentialType
import `fun`.adaptive.auth.model.Principal
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.lib.auth.crypto.BCrypt
import `fun`.adaptive.lib.auth.store.CredentialTable
import `fun`.adaptive.lib.auth.store.PrincipalTable
import `fun`.adaptive.utility.UUID
import kotlinx.datetime.Clock.System.now
import org.jetbrains.exposed.sql.transactions.transaction

class CookbookWorker : WorkerImpl<CookbookWorker> {

    override suspend fun run() {
        val admin = Principal(UUID(), "test", activated = true)
        val passwd = BCrypt.hashpw("test", BCrypt.gensalt())

        transaction {
            PrincipalTable.plusAssign(admin)
            CredentialTable.plusAssign(Credential(UUID(), admin.id, CredentialType.PASSWORD, passwd, now()))
        }
    }

}