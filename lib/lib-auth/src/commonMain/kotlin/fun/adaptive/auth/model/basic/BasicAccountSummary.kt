package `fun`.adaptive.auth.model.basic

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.properties
import `fun`.adaptive.auth.model.AuthPrincipalId
import `fun`.adaptive.auth.model.AuthRoleId
import `fun`.adaptive.auth.model.PrincipalSpec
import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValue
import kotlin.time.Instant

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

    constructor(principal: AvValue<PrincipalSpec>, account: AvValue<BasicAccountSpec>) : this(
        accountId = account.uuid,
        principalId = principal.uuid,
        login = principal.nameLike,
        name = account.nameLike,
        email = account.spec.email,
        activated = principal.spec.activated,
        locked = principal.spec.locked,
        lastLogin = principal.spec.lastAuthSuccess,
        roles = principal.spec.roles
    )

}