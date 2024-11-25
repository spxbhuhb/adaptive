package `fun`.adaptive.cookbook

import `fun`.adaptive.auth.model.Credential
import `fun`.adaptive.auth.model.CredentialType
import `fun`.adaptive.auth.model.Principal
import `fun`.adaptive.auth.model.Role
import `fun`.adaptive.auth.model.RoleGrant
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.cookbook.auth.AccountTable
import `fun`.adaptive.cookbook.auth.model.Account
import `fun`.adaptive.lib.auth.crypto.BCrypt
import `fun`.adaptive.lib.auth.service.PrincipalService
import `fun`.adaptive.lib.auth.store.CredentialTable
import `fun`.adaptive.lib.auth.store.PrincipalTable
import `fun`.adaptive.lib.auth.store.RoleGrantTable
import `fun`.adaptive.lib.auth.store.RoleTable
import `fun`.adaptive.utility.UUID
import kotlinx.datetime.Clock.System.now
import org.jetbrains.exposed.sql.transactions.transaction

class CookbookWorker : WorkerImpl<CookbookWorker> {

    override suspend fun run() {
        val role = Role(UUID(), "security-officer")
        val principal = Principal(UUID(), "so", activated = true)
        val account = Account(principal.id.cast(), "Security Officer", "", "")
        val passwd = BCrypt.hashpw("so", BCrypt.gensalt())
        val roleGrant = RoleGrant(principal.id, role.id)

        transaction {
            RoleTable.plusAssign(role)
            PrincipalTable.plusAssign(principal)
            AccountTable.plusAssign(account)
            CredentialTable.plusAssign(Credential(UUID(), principal.id, CredentialType.PASSWORD, passwd, now()))
            RoleGrantTable.plusAssign(roleGrant)
        }

        PrincipalService.addRoles += role.id
        PrincipalService.getRoles += role.id
        PrincipalService.updateRoles += role.id
    }

}