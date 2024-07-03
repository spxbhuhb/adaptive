/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.exposed

import hu.simplexion.adaptive.utility.UUID
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.EntityIDColumnType
import org.jetbrains.exposed.sql.EqOp
import org.jetbrains.exposed.sql.ExpressionWithColumnType
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNull
import org.jetbrains.exposed.sql.SqlExpressionBuilder.wrap

fun UUID<*>.asJvm() : java.util.UUID =
    java.util.UUID(this.msb, this.lsb)

fun <T> EntityID<java.util.UUID>.asCommon() : UUID<T> =
    UUID(this.value.mostSignificantBits, this.value.leastSignificantBits)

fun <T> java.util.UUID.asCommon() =
    UUID<T>(this.mostSignificantBits, this.leastSignificantBits)

infix fun <E : EntityID<java.util.UUID>?> ExpressionWithColumnType<E>.uuidEq(t: UUID<*>?): Op<Boolean> {
    if (t == null) return isNull()

    @Suppress("UNCHECKED_CAST")
    val table = (columnType as EntityIDColumnType<*>).idColumn.table as IdTable<java.util.UUID>
    val entityID = EntityID(t.asJvm(), table)
    return EqOp(this, wrap(entityID))
}