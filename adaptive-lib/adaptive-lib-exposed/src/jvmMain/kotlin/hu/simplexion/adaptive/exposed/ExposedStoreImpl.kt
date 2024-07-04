/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.exposed

import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.server.builtin.StoreImpl
import hu.simplexion.adaptive.utility.manualOrPlugin
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction

interface ExposedStoreImpl<A : AdatClass<A>, T : ExposedStoreImpl<A, T>> : StoreImpl<T> {

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

    fun add(valueFun: () -> A): InsertStatement<Number>

    fun add(value: A): InsertStatement<Number>

    fun isEmpty(): Boolean =
        count() == 0L

    fun isNotEmpty(): Boolean =
        count() != 0L

    fun count() =
        (this as Table).selectAll().count()

}