/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.auth.model

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatEntity
import hu.simplexion.adaptive.utility.UUID
import kotlinx.datetime.Instant

@Adat
class AuthHistoryEntry(

    override val id: UUID<AuthHistoryEntry>,

    val event: String,
    val executedBy: UUID<Principal>,
    val executedAt: Instant,

    val principal: UUID<Principal>?,
    val role: UUID<Role>?,
    val roleGroup: UUID<Role>?

) : AdatEntity<AuthHistoryEntry>