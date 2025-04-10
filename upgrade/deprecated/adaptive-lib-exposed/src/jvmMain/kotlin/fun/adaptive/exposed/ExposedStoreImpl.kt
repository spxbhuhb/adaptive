/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.exposed

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.backend.builtin.StoreImpl
import `fun`.adaptive.utility.manualOrPlugin
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction

interface ExposedStoreImpl<A : AdatClass, T : ExposedStoreImpl<A, T>> : StoreImpl<T> {

    override fun create() {
        if (this is Table) {
            transaction {
                SchemaUtils.createMissingTablesAndColumns(this@ExposedStoreImpl)
            }
        }
    }

    fun fromRow(row: ResultRow): A {
        manualOrPlugin("fromRow", row)
    }

    fun toRow(row: UpdateBuilder<*>, value: A) {
        manualOrPlugin("toRow", value)
    }

    @ExposedAdatSet
    fun <V> set(row: UpdateBuilder<*>, value: V, column: Column<V>) {
        row[column] = value
    }

    fun all(): List<A>

    fun add(value: () -> A): InsertStatement<Number>

    fun add(value: A): InsertStatement<Number>

    fun isEmpty(): Boolean =
        count() == 0L

    fun isNotEmpty(): Boolean =
        count() != 0L

    fun count() =
        (this as Table).selectAll().count()

}