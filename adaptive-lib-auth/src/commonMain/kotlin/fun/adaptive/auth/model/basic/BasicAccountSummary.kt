package `fun`.adaptive.auth.model.basic

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.properties
import `fun`.adaptive.auth.model.AuthPrincipalId
import `fun`.adaptive.auth.model.AuthRoleId
import `fun`.adaptive.auth.model.PrincipalSpec
import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.item.AvItem
import kotlinx.datetime.Instant

@Adat
class BasicAccountSummary(
    val accountId: AvValueId,
    val principalId: AuthPrincipalId,
    val login: String,
    val name: String,
    val email: String,
    val activated: Boolean,
    val locked: Boolean,
    val lastLogin: Instant?,
    val roles: Set<AuthRoleId>
) : SelfObservable<BasicAccountSummary>() {

    override fun descriptor() {
        properties {
            activated readonly true
            locked readonly true
        }
    }

    constructor(principal: AvItem<PrincipalSpec>, account: AvItem<BasicAccountSpec>) : this(
        accountId = account.uuid,
        principalId = principal.uuid,
        login = principal.name,
        name = account.name,
        email = account.spec.email,
        activated = principal.spec.activated,
        locked = principal.spec.locked,
        lastLogin = principal.spec.lastAuthSuccess,
        roles = principal.spec.roles
    )

}