/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.auth.model

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatCompanion
import hu.simplexion.adaptive.adat.AdatEntity
import hu.simplexion.adaptive.utility.UUID
import kotlinx.datetime.Instant

@Adat
class Session(
    override var id: UUID<Session>,
    var securityCode: String,
    val createdAt: Instant,
    var vmCreatedAt: Long,
    var lastActivity: Long,
    var principal: UUID<Principal>?,
    var roles: List<Role>
) : AdatEntity<Session> {

    companion object : AdatCompanion<Session> {
        val SESSION_TOKEN = UUID<Session>("7fdd494f-e542-4d5b-870b-7cab83dc3197")
        val LOGOUT_TOKEN = UUID<Session>("61e974dc-094b-42e8-b21c-08502be7c595")
    }

}