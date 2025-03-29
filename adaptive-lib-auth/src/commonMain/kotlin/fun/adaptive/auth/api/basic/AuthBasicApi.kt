package `fun`.adaptive.auth.api.basic

import `fun`.adaptive.auth.model.basic.BasicAccountSummary
import `fun`.adaptive.auth.model.basic.BasicSignUp
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface AuthBasicApi {

    /**
     * Get all accounts known to the application.
     */
    suspend fun accounts(): List<BasicAccountSummary>

    /**
     * Get the account that belongs to the caller or null if the caller is not logged in.
     */
    suspend fun account() : BasicAccountSummary?


    /**
     * Sign up for the application.
     */
    suspend fun signUp(signUp: BasicSignUp)

}