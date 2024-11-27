package `fun`.adaptive.cookbook.auth.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.AdatEntity
import `fun`.adaptive.auth.model.Credential
import `fun`.adaptive.utility.UUID

@Adat
class Account(
    override val id: UUID<Account>,
    val name: String,
    val email: String,
    val phone: String,
) : AdatEntity<Account> {
    companion object : AdatCompanion<Credential>
}