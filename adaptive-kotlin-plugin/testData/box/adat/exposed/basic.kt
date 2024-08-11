package `fun`.adaptive.exposed // this is important so lookups work fine

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.manualOrPlugin
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

@JvmName("asJavaN")
fun UUID<*>?.asJava(): java.util.UUID? =
    if (this == null) null else java.util.UUID(this.msb, this.lsb)

fun UUID<*>.asEntityID(table: UUIDTable): EntityID<java.util.UUID> =
    EntityID(this.asJava(), table)

@JvmName("asEntityIDN")
fun UUID<*>?.asEntityID(table: UUIDTable): EntityID<java.util.UUID>? =
    if (this == null) null else EntityID(asJava(), table)

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

@JvmName("asEntityIDN")
fun UUID<*>?.asEntityID(column: Column<java.util.UUID>): EntityID<java.util.UUID>? =
    this?.asEntityID(column)

fun <T> EntityID<java.util.UUID>.asCommon(): UUID<T> =
    UUID(this.value.mostSignificantBits, this.value.leastSignificantBits)

@JvmName("asCommonN")
fun <T> EntityID<java.util.UUID>?.asCommon(): UUID<T>? =
    this?.asCommon()

fun <T> java.util.UUID.asCommon() =
    UUID<T>(this.mostSignificantBits, this.leastSignificantBits)

@JvmName("asCommonN")
fun <T> java.util.UUID?.asCommon() =
    this?.asCommon<T>()

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