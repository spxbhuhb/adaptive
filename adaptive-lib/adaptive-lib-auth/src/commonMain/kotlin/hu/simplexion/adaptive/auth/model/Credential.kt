/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.auth.model

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatEntity
import hu.simplexion.adaptive.utility.UUID
import kotlinx.datetime.Instant

@Adat
class Credential(
    override val id: UUID<Credential>,
    var principal: UUID<Principal>,
    var type: String,
    var value: String,
    var createdAt: Instant
) : AdatEntity<Credential>