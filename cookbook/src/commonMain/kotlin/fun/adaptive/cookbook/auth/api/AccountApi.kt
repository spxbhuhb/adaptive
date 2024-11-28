package `fun`.adaptive.cookbook.auth.api

import `fun`.adaptive.cookbook.auth.model.AccountSummary
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface AccountApi {

    suspend fun accounts(): List<AccountSummary>

    /**
     * Get the account that belongs to the caller or null
     * if the caller is not logged in.
     */
    suspend fun account() : AccountSummary?

}