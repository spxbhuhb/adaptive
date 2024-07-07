/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.auth.store

import hu.simplexion.adaptive.auth.model.AuthHistoryEntry
import hu.simplexion.adaptive.auth.model.AuthenticationResult
import hu.simplexion.adaptive.exposed.AdatEntityTable
import hu.simplexion.adaptive.exposed.ExposedAdatTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

@ExposedAdatTable
object HistoryTable : AdatEntityTable<AuthHistoryEntry, HistoryTable>("auth_history") {

    val event = varchar("event", 100)

    val executedBy = reference("executed_by", PrincipalTable)
    val executedAt = timestamp("executed_at")

    val session = uuid("session").nullable()
    val principal = reference("principal", PrincipalTable).nullable()
    val role = reference("role", RoleTable).nullable()
    val roleGroup = reference("role_group", RoleTable).nullable()

    val result = enumerationByName<AuthenticationResult>("result", 40).nullable()
}