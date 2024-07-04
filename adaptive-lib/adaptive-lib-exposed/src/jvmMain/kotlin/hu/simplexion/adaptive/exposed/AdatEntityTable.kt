/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.exposed

import hu.simplexion.adaptive.adat.AdatEntity
import hu.simplexion.adaptive.utility.UUID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

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

    override fun add(valueFun: () -> A) =
        add(valueFun())

    override fun add(value: A) =
        insert { toRow(it, value) }

    open fun update(valueFun: () -> A) =
        update(valueFun())

    open fun update(value: A) =
        update({ this@AdatEntityTable.id uuidEq value.id }) { toRow(it, value) }

    open fun remove(idFun: () -> UUID<A>) =
        remove(idFun())

    open fun remove(id: UUID<A>) =
        deleteWhere { this.id uuidEq id }

}