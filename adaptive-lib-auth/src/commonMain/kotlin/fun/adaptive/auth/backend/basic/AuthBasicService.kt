package `fun`.adaptive.auth.backend.basic

import `fun`.adaptive.adat.ensureValid
import `fun`.adaptive.auth.api.basic.AuthBasicApi
import `fun`.adaptive.auth.backend.AuthPrincipalService
import `fun`.adaptive.auth.backend.AuthWorker
import `fun`.adaptive.auth.context.ensureHas
import `fun`.adaptive.auth.context.ensurePrincipalOrHas
import `fun`.adaptive.auth.context.getPrincipalIdOrNull
import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.auth.model.AuthMarkers
import `fun`.adaptive.auth.model.CredentialType.PASSWORD
import `fun`.adaptive.auth.model.PrincipalSpec
import `fun`.adaptive.auth.model.basic.BasicAccountSpec
import `fun`.adaptive.auth.model.basic.BasicAccountSummary
import `fun`.adaptive.auth.model.basic.BasicSignUp
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
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
                accountSummary(it, account.asAvItem<BasicAccountSpec>())
            }
        }
    }

    override suspend fun account(): BasicAccountSummary? {
        publicAccess()

        val principalId = serviceContext.getPrincipalIdOrNull() ?: return null

        val principal = valueWorker.item(principalId).withSpec<PrincipalSpec>()
        val account = valueWorker.refItem<BasicAccountSpec>(principal, AuthMarkers.ACCOUNT_REF)

        return accountSummary(principal, account)
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

    override suspend fun save(principalId : AvValueId, name : String, spec: BasicAccountSpec) {
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

    private fun accountSummary(principal : AvItem<PrincipalSpec>, account : AvItem<BasicAccountSpec>) =
        BasicAccountSummary(
            accountId = account.uuid,
            principalId = principal.uuid,
            login = principal.name,
            name = account.name,
            email = account.spec.email,
            activated = principal.spec.activated,
            locked = principal.spec.locked,
            lastLogin = principal.spec.lastAuthSuccess
        )
}