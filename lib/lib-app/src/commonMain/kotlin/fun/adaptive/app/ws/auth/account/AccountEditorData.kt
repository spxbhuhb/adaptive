package `fun`.adaptive.app.ws.auth.account

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.Secret
import `fun`.adaptive.adat.api.properties
import `fun`.adaptive.value.AvValueId

@Adat
class AccountEditorData(
    val principalId : AvValueId? = null,
    val accountId : AvValueId? = null,
    val principalName: String = "",
    val name: String = "",
    val email: String = "",
    val activated: Boolean = false,
    val locked: Boolean = false,
    val password: Secret = "",
    val passwordConfirm: Secret = "",
    val roles : Set<AvValueId> = emptySet()
) {
    override fun descriptor() {
        properties {
            principalName blank false
            name blank false minLength 5 maxLength 255
            email blank false pattern "^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"
        }
    }
}