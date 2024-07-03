package hu.simplexion.adaptive.exposed // this is important so lookups work fine

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.utility.manualOrPlugin
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder

@Adat
class TestAdat(
    val id: UUID<TestAdat>,
    val someUuid: UUID<TestAdat>,
    val someInt: Int,
    var someBoolean: Boolean
) : AdatClass<TestAdat>

@ExposedAdatTable
class TestAdatTable : AbstractAdatTable<TestAdat>() {
    val someUuid = uuid("some_uuid")
    val someInt = integer("some_int")
    val someBoolean = bool("some_boolean")
}

fun box(): String {
    val t1 = TestAdat(UUID(), UUID(), 1, false)
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