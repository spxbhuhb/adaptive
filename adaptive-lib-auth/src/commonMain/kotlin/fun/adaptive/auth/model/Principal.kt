/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.AdatEntity
import `fun`.adaptive.utility.UUID
import kotlinx.datetime.Instant

@Adat
class Principal(
    override var id: UUID<Principal>,
    var principalName: String,
    var activated: Boolean = false,
    var locked: Boolean = false,
    var expired: Boolean = false,
    var anonymized: Boolean = false,
    var lastAuthSuccess: Instant? = null,
    var authSuccessCount: Int = 0,
    var lastAuthFail: Instant? = null,
    var authFailCount: Int = 0
) : AdatEntity<Principal> {
    companion object : AdatCompanion<Principal>
}
