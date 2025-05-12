package `fun`.adaptive.auth.backend.basic

import `fun`.adaptive.adat.ensureValid
import `fun`.adaptive.auth.api.basic.AuthBasicApi
import `fun`.adaptive.auth.app.AuthModule
import `fun`.adaptive.auth.backend.*
import `fun`.adaptive.auth.context.*
import `fun`.adaptive.auth.model.AuthMarkers
import `fun`.adaptive.auth.model.Credential
import `fun`.adaptive.auth.model.CredentialList
import `fun`.adaptive.auth.model.CredentialType.PASSWORD
import `fun`.adaptive.auth.model.PrincipalSpec
import `fun`.adaptive.auth.model.basic.BasicAccountSpec
import `fun`.adaptive.auth.model.basic.BasicAccountSummary
import `fun`.adaptive.auth.model.basic.BasicSignUp
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.lib.util.error.requirement
import `fun`.adaptive.value.*
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValue.Companion.asAvValue
import `fun`.adaptive.value.AvValue.Companion.withSpec
import `fun`.adaptive.value.store.AvComputeContext
import `fun`.adaptive.value.util.serviceSubscribe
import kotlinx.datetime.Clock.System.now

class AuthBasicService : ServiceImpl<AuthBasicService>(), AuthBasicApi {

    val authWorker by worker<AuthWorker>()
    val valueWorker by worker<AvValueWorker>()
    val securityOfficer by lazy { authWorker.securityOfficer }

    override suspend fun accounts(): List<BasicAccountSummary> {
        ensureHas(securityOfficer)

        val accounts = valueWorker.queryByMarker(AuthMarkers.BASIC_ACCOUNT)
        val principals = valueWorker.queryByMarker(AuthMarkers.PRINCIPAL).associateBy { it.uuid }

        return accounts.mapNotNull { account ->
            principals[account.parentId]?.asAvValue<PrincipalSpec>()?.let {
                BasicAccountSummary(it, account.asAvValue<BasicAccountSpec>())
            }
        }
    }

    override suspend fun subscribe(subscriptionId: AvSubscriptionId): List<AvSubscribeCondition> {
        ensureHas(securityOfficer)
        return serviceSubscribe(valueWorker, subscriptionId, AuthMarkers.BASIC_ACCOUNT, AuthMarkers.PRINCIPAL)
    }

    override suspend fun unsubscribe(subscriptionId: AvSubscriptionId) {
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

        val accountValue = AvValue(
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

    override suspend fun save(
        principalId: AvValueId?,
        principalName: String,
        principalSpec: PrincipalSpec,
        credential: Credential?,
        accountName: String,
        accountSpec: BasicAccountSpec,
        callCredential: Credential?
    ): AvValueId {
        ensurePrincipalOrHas(principalId, securityOfficer)

        if (principalId == null) {
            // in this case we have a security officer for sure as ensure got a null principal
            return add(principalName, credential !!, principalSpec.roles, accountName, accountSpec)
        }

        val originalPrincipal = valueWorker.item(principalId).withSpec<PrincipalSpec>()
        val originalAccount = valueWorker.refItem<BasicAccountSpec>(principalId, AuthMarkers.ACCOUNT_REF)

        // credential, e-mail and login name change of own account requires the call to supply valid credentials

        val sensitive = (
            credential != null
                || principalName != originalPrincipal.name
                || accountSpec.email != originalAccount.spec.email
            )

        if (principalId == serviceContext.getPrincipalId() && sensitive) {
            checkNotNull(callCredential)
            getSessionService(securityOfficer).authenticate(
                principalId,
                callCredential.value,
                checkCredentials = true,
                callCredential.type,
                AuthPrincipalService().policy
            )
        }

        // roles can be changed only by the security officer

        val roleChange = (principalSpec.roles != originalPrincipal.spec.roles)
        require(! roleChange || serviceContext.has(securityOfficer))

        // apply the changes

        valueWorker.execute {
            update(principalId, principalName, principalSpec, credential, accountName, accountSpec)
        }

        return principalId
    }

    private suspend fun add(
        principalName: String,
        credential: Credential,
        roles: Set<AvValueId>,
        accountName: String,
        accountSpec: BasicAccountSpec
    ): AvValueId {
        val account = AvValue(
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

    private fun AvComputeContext.update(
        principalId: AvValueId,
        principalName: String,
        principalSpec: PrincipalSpec,
        credential: Credential?,
        accountName: String,
        accountSpec: BasicAccountSpec
    ) {
        val originalPrincipal = get<PrincipalSpec>(principalId)
        val originalAccount = ref<BasicAccountSpec>(originalPrincipal, AuthMarkers.ACCOUNT_REF)

        val uniqueName = valueWorker.queryByMarker(AuthMarkers.PRINCIPAL).none {
            it.name == principalName && it.uuid != principalId
        }
        requirement(AuthModule.ALREADY_EXISTS) { uniqueName }

        update(originalPrincipal, principalName, principalSpec)
        update(originalAccount, accountName, accountSpec)
        update(originalPrincipal, credential)
    }

    private fun AvComputeContext.update(
        currentPrincipal: AvValue<PrincipalSpec>,
        name: String,
        spec: PrincipalSpec
    ) {
        val copy = currentPrincipal.copy(
            name = name,
            spec = currentPrincipal.spec.copy(
                activated = spec.activated,
                locked = spec.locked,
                authFailCount = if (currentPrincipal.spec.locked && ! spec.locked) 0 else currentPrincipal.spec.authFailCount,
                roles = spec.roles
            )
        )

        if (copy != currentPrincipal) {
            this += copy
        }
    }

    private fun AvComputeContext.update(
        currentAccount: AvValue<BasicAccountSpec>,
        name: String,
        spec: BasicAccountSpec
    ) {
        val copy = currentAccount.copy(
            name = name,
            spec = currentAccount.spec.copy(email = spec.email)
        )

        if (copy != currentAccount) {
            this += copy
        }
    }

    private fun AvComputeContext.update(
        currentPrincipal: AvValue<PrincipalSpec>,
        credential: Credential?
    ) {
        if (credential == null) return

        val credentialList = markerVal<CredentialList>(currentPrincipal, AuthMarkers.CREDENTIAL_LIST)

        val newSet = credentialList.credentials.toMutableSet()
        newSet.removeAll { it.type == credential.type }
        newSet.add(credential.hash())

        this += credentialList.copy(timestamp = now(), credentials = newSet)
    }

}