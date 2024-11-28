package `fun`.adaptive.cookbook.auth

import `fun`.adaptive.auth.context.ensureOneOf
import `fun`.adaptive.auth.context.getPrincipalIdOrNull
import `fun`.adaptive.auth.context.getSession
import `fun`.adaptive.auth.context.getSessionOrNull
import `fun`.adaptive.auth.context.isLoggedIn
import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.auth.model.Principal
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.cookbook.auth.api.AccountApi
import `fun`.adaptive.cookbook.auth.model.Account
import `fun`.adaptive.cookbook.auth.model.AccountSummary
import `fun`.adaptive.lib.auth.service.PrincipalService
import `fun`.adaptive.lib.auth.store.PrincipalTable

class AccountService : ServiceImpl<AccountService>, AccountApi {

    override suspend fun accounts(): List<AccountSummary> {
        ensureOneOf(*PrincipalService.getRoles)

        val accounts = AccountTable.all()
        val principals = PrincipalTable.all().associateBy { it.id }

        return accounts.map { account ->
            accountSummary(principals[account.id.cast()] !!, account)
        }
    }

    override suspend fun account(): AccountSummary? {
        publicAccess()

        val principalId = serviceContext.getPrincipalIdOrNull() ?: return null

        return accountSummary(
            PrincipalTable[principalId],
            AccountTable[principalId.cast()]
        )
    }

    private fun accountSummary(principal : Principal, account : Account) =
        AccountSummary(
            id = account.id,
            login = principal.name,
            name = account.name,
            email = account.email,
            phone = account.phone,
            activated = principal.activated,
            locked = principal.locked,
            lastLogin = principal.lastAuthSuccess
        )
}