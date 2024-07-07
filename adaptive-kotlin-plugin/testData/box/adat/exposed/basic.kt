package hu.simplexion.adaptive.exposed // this is important so lookups work fine

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.utility.manualOrPlugin
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder

@Adat
class TestAdat(
    val id: UUID<TestAdat>,
    val someUuid: UUID<TestAdat>,
    val someUuidNullable: UUID<TestAdat>?,
    val someReferenceNullable: UUID<TestAdat>?,
    val someInt: Int,
    var someBoolean: Boolean
) : AdatClass<TestAdat>

@ExposedAdatTable
object TestAdatTable : AbstractAdatTable<TestAdat>() {
    val someUuid = uuid("some_uuid")
    val someUuidNullable = uuid("some_uuid_nullable").nullable()
    val someReferenceNullable = reference("some_reference", TestAdatTable).nullable()
    val someInt = integer("some_int")
    val someBoolean = bool("some_boolean")
}

fun box(): String {
    val t1 = TestAdat(UUID(), UUID(), null, null, 1, false)
    return "OK"
}

// ----------------------------------------------------------------------------
// These are mostly copied from the exposed package to make the test compile
// ----------------------------------------------------------------------------

annotation class ExposedAdatSet

annotation class ExposedAdatTable

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

abstract class AbstractAdatTable<A : AdatClass<A>> : UUIDTable("test") {

    open fun fromRow(row: ResultRow): A {
        manualOrPlugin("fromRow", row)
    }

    open fun toRow(row: UpdateBuilder<*>, value: A) {
        manualOrPlugin("toRow", value)
    }

    @ExposedAdatSet
    fun <V> set(row: UpdateBuilder<*>, value: V, column: Column<V>) {
        row[column] = value
    }

}