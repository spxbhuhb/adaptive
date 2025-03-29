package `fun`.adaptive.app.basic.auth.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.AdatEntity
import `fun`.adaptive.auth.model.Credential
import `fun`.adaptive.utility.UUID

@Adat
class BasicAccount(
    override val id: UUID<BasicAccount>,
    val name: String,
    val email: String,
    val phone: String,
)