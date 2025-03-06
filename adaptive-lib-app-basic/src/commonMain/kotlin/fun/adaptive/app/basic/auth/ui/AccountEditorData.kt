package `fun`.adaptive.app.basic.auth.ui

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.properties
import `fun`.adaptive.app.basic.auth.model.BasicAccount
import `fun`.adaptive.utility.UUID

@Adat
class AccountEditorData(
    val id: UUID<BasicAccount>? = null,
    val login: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val activated: Boolean = false,
    val locked: Boolean = false,
) {
    override fun descriptor() {
        properties {
            login blank false
            name blank false minLength 5 maxLength 255
            email blank false pattern "^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"
        }
    }
}