/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.auth.store

import hu.simplexion.adaptive.auth.model.Principal
import hu.simplexion.adaptive.exposed.AdatEntityTable
import hu.simplexion.adaptive.exposed.ExposedAdatTable
import hu.simplexion.adaptive.exposed.uuidEq
import hu.simplexion.adaptive.utility.UUID
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

@ExposedAdatTable
object PrincipalTable : AdatEntityTable<Principal, PrincipalTable>("auth_principal") {

    val name = text("name").uniqueIndex()
    val activated = bool("activated")
    val locked = bool("locked")
    val expired = bool("expired")
    val anonymized = bool("anonymized")
    val lastAuthSuccess = timestamp("lastAuthSuccess").nullable()
    val authSuccessCount = integer("authSuccessCount")
    val lastAuthFail = timestamp("lastAuthFail").nullable()
    val authFailCount = integer("authFailCount")

    fun setLocked(uuid: UUID<Principal>, locked: Boolean) {
        update({ id uuidEq uuid }) {
            it[this.locked] = locked
        }
    }

    fun setActivated(uuid: UUID<Principal>, activated: Boolean) {
        update({ id uuidEq uuid }) {
            it[this.activated] = activated
        }
    }

    fun setName(uuid: UUID<Principal>, name: String) {
        update({ id uuidEq uuid }) {
            it[this.name] = name
        }
    }

    fun getByNameOrNull(inName: String): Principal? =
        select { name eq inName }
            .map { fromRow(it) }
            .firstOrNull()

}