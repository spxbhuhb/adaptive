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
class Credential(
    override val id: UUID<Credential>,
    var principal: UUID<Principal>,
    var type: String,
    var value: String,
    var createdAt: Instant
) : AdatEntity<Credential> {
    companion object : AdatCompanion<Credential>
}