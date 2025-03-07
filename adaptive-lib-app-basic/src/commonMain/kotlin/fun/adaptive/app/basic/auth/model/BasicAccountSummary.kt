package `fun`.adaptive.app.basic.auth.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.properties
import `fun`.adaptive.utility.UUID
import kotlinx.datetime.Instant

@Adat
class BasicAccountSummary(
    val id: UUID<BasicAccount>,
    val login: String,
    val name: String,
    val email: String,
    val phone: String,
    val activated: Boolean,
    val locked: Boolean,
    val lastLogin: Instant?,
) {
    override fun descriptor() {
        properties {
            activated readonly true
            locked readonly true
        }
    }
}