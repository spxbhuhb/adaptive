package `fun`.adaptive.auth.model.basic

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.properties
import `fun`.adaptive.auth.model.AuthPrincipalId
import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.value.AvValueId
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
    val lastLogin: Instant?
) : SelfObservable<BasicAccountSummary>() {

    override fun descriptor() {
        properties {
            activated readonly true
            locked readonly true
        }
    }

}