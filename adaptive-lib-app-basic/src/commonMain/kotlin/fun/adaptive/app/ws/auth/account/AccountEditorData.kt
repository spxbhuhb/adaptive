package `fun`.adaptive.app.ws.auth.account

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.properties
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.value.AvValueId

@Adat
class AccountEditorData(
    val uuid : AvValueId = uuid4(),
    val login: String = "",
    val name: String = "",
    val email: String = "",
    val activated: Boolean = false,
    val locked: Boolean = false,
    val roles : Set<String> = emptySet()
) {
    override fun descriptor() {
        properties {
            login blank false
            name blank false minLength 5 maxLength 255
            email blank false pattern "^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"
        }
    }
}