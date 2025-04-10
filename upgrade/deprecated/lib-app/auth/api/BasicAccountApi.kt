package `fun`.adaptive.app.basic.auth.api

import `fun`.adaptive.app.basic.auth.model.BasicAccountSummary
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface BasicAccountApi {

    suspend fun accounts(): List<BasicAccountSummary>

    /**
     * Get the account that belongs to the caller or null
     * if the caller is not logged in.
     */
    suspend fun account() : BasicAccountSummary?

}