package `fun`.adaptive.auth.api.basic

import `fun`.adaptive.auth.model.Credential
import `fun`.adaptive.auth.model.PrincipalSpec
import `fun`.adaptive.auth.model.basic.BasicAccountSpec
import `fun`.adaptive.auth.model.basic.BasicAccountSummary
import `fun`.adaptive.auth.model.basic.BasicSignUp
import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvSubscriptionId
import `fun`.adaptive.value.remote.AvPublisher

@ServiceApi
interface AuthBasicApi : AvPublisher {

    /**
     * Get all accounts known to the application.
     */
    suspend fun accounts(): List<BasicAccountSummary>

    /**
     * Subscribe for principals and accounts. Whenever one of
     * these changes, the change will be sent to the client.
     */
    override suspend fun subscribe(subscriptionId: AvSubscriptionId): List<AvSubscribeCondition>

    override suspend fun unsubscribe(subscriptionId: AvSubscriptionId)

    /**
     * Get the account that belongs to the caller or null if the caller is not logged in.
     */
    suspend fun account(): BasicAccountSummary?

    /**
     * Sign up for the application.
     */
    suspend fun signUp(signUp: BasicSignUp)

    /**
     * Adds (when [principalId] is null) or updates a principal and an account.
     */
    suspend fun save(
        principalId: AvValueId?,
        principalName: String,
        principalSpec: PrincipalSpec,
        credential: Credential?,
        accountName: String,
        accountSpec: BasicAccountSpec,
        callCredential: Credential? = null
    ): AvValueId


}