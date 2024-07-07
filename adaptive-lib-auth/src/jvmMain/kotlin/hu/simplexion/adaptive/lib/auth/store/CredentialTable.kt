/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.auth.store

import hu.simplexion.adaptive.auth.model.Credential
import hu.simplexion.adaptive.auth.model.CredentialType.ACTIVATION_KEY
import hu.simplexion.adaptive.auth.model.CredentialType.PASSWORD_RESET_KEY
import hu.simplexion.adaptive.auth.model.Principal
import hu.simplexion.adaptive.exposed.AdatEntityTable
import hu.simplexion.adaptive.exposed.ExposedAdatTable
import hu.simplexion.adaptive.exposed.uuidEq
import hu.simplexion.adaptive.utility.UUID
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.select

@ExposedAdatTable
object CredentialTable : AdatEntityTable<Credential, CredentialTable>("auth_credential") {

    val principal = reference("principal", PrincipalTable)
    val type = varchar("type", 50)
    val value = text("value")
    val createdAt = timestamp("created_at")

    fun readValue(principalId: UUID<Principal>, credentialType: String): String? =
        slice(value)
            .select {
                (principal uuidEq principalId) and (type eq credentialType)
            }
            .orderBy(createdAt, SortOrder.DESC)
            .limit(1)
            .firstOrNull()
            ?.let { it[value] }


    fun removeActivationKeys(principalId: UUID<Principal>) {
        deleteWhere { (principal uuidEq principalId) and (type eq ACTIVATION_KEY) }
    }

    fun removePasswordResetKeys(principalId: UUID<Principal>) {
        deleteWhere { (principal uuidEq principalId) and (type eq PASSWORD_RESET_KEY) }
    }

}