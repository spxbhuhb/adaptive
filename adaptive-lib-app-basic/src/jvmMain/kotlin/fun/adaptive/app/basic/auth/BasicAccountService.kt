package `fun`.adaptive.app.basic.auth

import `fun`.adaptive.app.basic.auth.api.BasicAccountApi
import `fun`.adaptive.app.basic.auth.model.BasicAccount
import `fun`.adaptive.app.basic.auth.model.BasicAccountSummary
import `fun`.adaptive.auth.context.ensureOneOf
import `fun`.adaptive.auth.context.getPrincipalIdOrNull
import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.auth.model.Principal
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.auth.backend.AuthPrincipalService
import `fun`.adaptive.lib.auth.store.PrincipalTable

class BasicAccountService : ServiceImpl<BasicAccountService>, BasicAccountApi {

    override suspend fun accounts(): List<BasicAccountSummary> {
        ensureOneOf(*AuthPrincipalService.getRoles)

        val accounts = BasicAccountTable.all()
        val principals = PrincipalTable.all().associateBy { it.id }

        return accounts.map { account ->
            accountSummary(principals[account.id.cast()] !!, account)
        }
    }

    override suspend fun account(): BasicAccountSummary? {
        publicAccess()

        val principalId = serviceContext.getPrincipalIdOrNull() ?: return null

        return accountSummary(
            PrincipalTable[principalId],
            BasicAccountTable[principalId.cast()]
        )
    }

    private fun accountSummary(principal : Principal, account : BasicAccount) =
        BasicAccountSummary(
            id = account.id,
            login = principal.principalName,
            name = account.name,
            email = account.email,
            phone = account.phone,
            activated = principal.activated,
            locked = principal.locked,
            lastLogin = principal.lastAuthSuccess
        )
}