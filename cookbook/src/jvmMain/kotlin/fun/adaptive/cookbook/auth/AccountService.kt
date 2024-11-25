package `fun`.adaptive.cookbook.auth

import `fun`.adaptive.auth.context.ensureOneOf
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.cookbook.auth.api.AccountApi
import `fun`.adaptive.cookbook.auth.model.AccountSummary
import `fun`.adaptive.lib.auth.service.PrincipalService
import `fun`.adaptive.lib.auth.store.PrincipalTable

class AccountService : ServiceImpl<AccountService>, AccountApi {

    override suspend fun accounts(): List<AccountSummary> {
        ensureOneOf(*PrincipalService.getRoles)

        val accounts = AccountTable.all()
        val principals = PrincipalTable.all().associateBy { it.id }

        return accounts.map { account ->
            val principal = principals[account.id.cast()] !!
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
    }

}