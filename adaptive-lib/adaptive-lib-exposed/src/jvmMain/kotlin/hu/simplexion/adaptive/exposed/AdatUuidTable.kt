/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.exposed

import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.utility.UUID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.update

/**
 * A [UUIDTable] with Adat field mapping. The compiler plugin generates
 * `fromRow` and `toRow` functions to map between Exposed and the Adat
 * class this table stores.
 *
 * Check the reference documentation for exact details of the mapping.
 */
abstract class AdatUuidTable<A : AdatClass<A>, S : AdatUuidTable<A, S>>(
    tableName: String = "",
    columnName: String = "uuid"
) : UUIDTable(tableName, columnName), ExposedStoreImpl<A, S> {

    open fun update(uuid: UUID<A>, value: A) =
        update({ id uuidEq uuid }) { toRow(it, value) }

    open fun remove(uuid: UUID<A>) =
        deleteWhere { id uuidEq uuid }

}