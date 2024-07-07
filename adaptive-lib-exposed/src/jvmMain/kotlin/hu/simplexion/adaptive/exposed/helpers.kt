/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.exposed

import hu.simplexion.adaptive.utility.UUID
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNull
import org.jetbrains.exposed.sql.SqlExpressionBuilder.wrap

fun UUID<*>.asJava(): java.util.UUID =
    java.util.UUID(this.msb, this.lsb)

fun UUID<*>.asEntityID(table: UUIDTable): EntityID<java.util.UUID> =
    EntityID(this.asJava(), table)

/**
 * Creates an Exposed [EntityID] for a `reference` column. Gets the table
 * from `column.foreignKey`.
 *
 * @throws  IllegalStateException  if the column does not have a foreign key
 */
@Suppress("UNCHECKED_CAST")
fun UUID<*>.asEntityID(column: Column<java.util.UUID>): EntityID<java.util.UUID> {
    val fk = column.foreignKey
    if (fk != null) {
        return EntityID(this.asJava(), fk.targetTable as IdTable<java.util.UUID>)
    } else {
        // this is the case of the own `id` column
        return EntityID(this.asJava(), column.table as IdTable<java.util.UUID>)
    }
}

fun <T> EntityID<java.util.UUID>.asCommon(): UUID<T> =
    UUID(this.value.mostSignificantBits, this.value.leastSignificantBits)

fun <T> java.util.UUID.asCommon() =
    UUID<T>(this.mostSignificantBits, this.leastSignificantBits)

infix fun <E : EntityID<java.util.UUID>?> ExpressionWithColumnType<E>.uuidEq(t: UUID<*>?): Op<Boolean> {
    if (t == null) return isNull()

    @Suppress("UNCHECKED_CAST")
    val table = (columnType as EntityIDColumnType<*>).idColumn.table as IdTable<java.util.UUID>
    val entityID = EntityID(t.asJava(), table)
    return EqOp(this, wrap(entityID))
}