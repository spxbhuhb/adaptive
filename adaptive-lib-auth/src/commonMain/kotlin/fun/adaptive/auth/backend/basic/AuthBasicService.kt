package `fun`.adaptive.auth.backend.basic

import `fun`.adaptive.adat.ensureValid
import `fun`.adaptive.auth.api.basic.AuthBasicApi
import `fun`.adaptive.auth.backend.AuthPrincipalService
import `fun`.adaptive.auth.backend.AuthWorker
import `fun`.adaptive.auth.backend.getPrincipalService
import `fun`.adaptive.auth.context.ensureHas
import `fun`.adaptive.auth.context.ensurePrincipalOrHas
import `fun`.adaptive.auth.context.ensuredByLogic
import `fun`.adaptive.auth.context.getPrincipalIdOrNull
import `fun`.adaptive.auth.model.AuthMarkers
import `fun`.adaptive.auth.model.Credential
import `fun`.adaptive.auth.model.CredentialType.PASSWORD
import `fun`.adaptive.auth.model.PrincipalSpec
import `fun`.adaptive.auth.model.basic.BasicAccountSpec
import `fun`.adaptive.auth.model.basic.BasicAccountSummary
import `fun`.adaptive.auth.model.basic.BasicSignUp
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.value.AvClientSubscription
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueSubscriptionId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvItem.Companion.asAvItem
import `fun`.adaptive.value.item.AvItem.Companion.withSpec

class AuthBasicService : ServiceImpl<AuthBasicService>, AuthBasicApi {

    val authWorker by worker<AuthWorker>()
    val valueWorker by worker<AvValueWorker>()
    val securityOfficer by lazy { authWorker.securityOfficer }

    override suspend fun accounts(): List<BasicAccountSummary> {
        ensureHas(securityOfficer)

        val accounts = valueWorker.queryByMarker(AuthMarkers.BASIC_ACCOUNT)
        val principals = valueWorker.queryByMarker(AuthMarkers.PRINCIPAL).associateBy { it.uuid }

        return accounts.mapNotNull { account ->
            principals[account.parentId]?.asAvItem<PrincipalSpec>()?.let {
                BasicAccountSummary(it, account.asAvItem<BasicAccountSpec>())
            }
        }
    }

    override suspend fun subscribe(subscriptionId: AvValueSubscriptionId): List<AvSubscribeCondition> {
        ensureHas(securityOfficer)

        val conditions = listOf(
            AvSubscribeCondition(marker = AuthMarkers.BASIC_ACCOUNT),
            AvSubscribeCondition(marker = AuthMarkers.PRINCIPAL)
        )

        val subscription = AvClientSubscription(
            subscriptionId,
            conditions = conditions,
            transport = serviceContext.transport,
            scope = safeAdapter.scope
        )

        valueWorker.subscribe(subscription)

        return conditions
    }

    override suspend fun unsubscribe(subscriptionId: AvValueSubscriptionId) {
        ensureHas(securityOfficer)

        valueWorker.unsubscribe(subscriptionId)
    }

    override suspend fun account(): BasicAccountSummary? {
        ensuredByLogic("the user can access his/her own account")

        val principalId = serviceContext.getPrincipalIdOrNull() ?: return null

        val principal = valueWorker.item(principalId).withSpec<PrincipalSpec>()
        val account = valueWorker.refItem<BasicAccountSpec>(principal, AuthMarkers.ACCOUNT_REF)

        return BasicAccountSummary(principal, account)
    }

    override suspend fun signUp(signUp: BasicSignUp) {
        ensureHas(securityOfficer)
        ensureValid(signUp)

        val accountValue = AvItem<BasicAccountSpec>(
            name = signUp.name,
            type = AuthMarkers.BASIC_ACCOUNT,
            friendlyId = signUp.name,
            spec = BasicAccountSpec(
                email = signUp.email
            )
        )

        AuthPrincipalService().addPrincipal(
            signUp.email, // principal name will be the e-mail
            PrincipalSpec(),
            PASSWORD,
            signUp.password,
            accountValue
        )
    }

    override suspend fun add(
        principalName: String,
        principalSpec: PrincipalSpec,
        credential: Credential,
        roles: Set<AvValueId>,
        accountName: String,
        accountSpec: BasicAccountSpec
    ): AvValueId {

        val account = AvItem<BasicAccountSpec>(
            name = accountName,
            type = AuthMarkers.BASIC_ACCOUNT,
            friendlyId = accountName.split(" ").mapNotNull { it.firstOrNull()?.toString()?.uppercase() }.take(2).joinToString(""),
            spec = accountSpec,
            markersOrNull = mutableMapOf(AuthMarkers.BASIC_ACCOUNT to null)
        )

        getPrincipalService(securityOfficer).addPrincipal(
            principalName,
            PrincipalSpec(activated = true, roles = roles),
            credential.type,
            credential.value,
            account
        ).also {
            return it
        }
    }

    override suspend fun save(principalId: AvValueId, name: String, spec: BasicAccountSpec) {
        ensurePrincipalOrHas(principalId, securityOfficer)
        ensureValid(spec)

        val accountId = valueWorker.ref(principalId, AuthMarkers.ACCOUNT_REF)

        valueWorker.update<AvValue>(accountId) { item ->
            item.asAvItem<BasicAccountSpec>().copy(
                name = name,
                spec = spec
            )
        }
    }
}