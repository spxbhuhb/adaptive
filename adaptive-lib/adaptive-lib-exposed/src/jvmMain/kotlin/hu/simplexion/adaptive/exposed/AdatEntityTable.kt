/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.exposed

import hu.simplexion.adaptive.adat.AdatEntity
import hu.simplexion.adaptive.utility.UUID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*

/**
 * A [UUIDTable] with Adat field mapping. The compiler plugin generates
 * `fromRow` and `toRow` functions to map between Exposed and the Adat
 * class this table stores.
 *
 * Check the reference documentation for exact details of the mapping.
 */
abstract class AdatEntityTable<A : AdatEntity<A>, S : AdatEntityTable<A, S>>(
    tableName: String = "",
    columnName: String = "uuid"
) : UUIDTable(tableName, columnName), ExposedStoreImpl<A, S> {

    override fun all(): List<A> =
        selectAll().map { fromRow(it) }

    // ---- get -----------------------------------------------------------------------

    operator fun get(valueId: UUID<A>) =
        requireNotNull(getOrNull(valueId))

    fun getOrNull(valueId: UUID<A>) =
        select { id uuidEq valueId }
            .map { fromRow(it) }
            .singleOrNull()

    // ---- add -----------------------------------------------------------------------

    override fun add(value: () -> A) =
        add(value())

    override fun add(value: A) =
        insert { toRow(it, value) }

    operator fun plusAssign(value: A) {
        add(value)
    }

    // ---- update -----------------------------------------------------------------------

    open fun update(value: () -> A) =
        update(value())

    open fun update(value: A) =
        update({ this@AdatEntityTable.id uuidEq value.id }) { toRow(it, value) }

    operator fun remAssign(value: A) {
        update(value)
    }

    // ---- remove -----------------------------------------------------------------------

    open fun remove(idFun: () -> UUID<A>) =
        remove(idFun())

    open fun remove(id: UUID<A>) =
        deleteWhere { this.id uuidEq id }

    operator fun minusAssign(id: UUID<A>) {
        remove(id)
    }

    operator fun minusAssign(value: A) {
        remove(value.id)
    }

}