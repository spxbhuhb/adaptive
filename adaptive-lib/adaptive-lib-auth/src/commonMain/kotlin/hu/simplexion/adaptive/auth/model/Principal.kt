/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.auth.model

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.AdatEntity
import hu.simplexion.adaptive.utility.UUID
import kotlinx.datetime.Instant

@Adat
class Principal(
    override var id: UUID<Principal>,
    var name: String,
    var activated: Boolean = false,
    var locked: Boolean = false,
    var expired: Boolean = false,
    var anonymized: Boolean = false,
    var lastAuthSuccess: Instant?,
    var authSuccessCount: Int,
    var lastAuthFail: Instant?,
    var authFailCount: Int
) : AdatEntity<Principal>
