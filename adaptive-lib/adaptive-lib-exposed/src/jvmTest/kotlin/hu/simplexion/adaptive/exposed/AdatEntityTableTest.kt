/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.exposed

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatEntity
import hu.simplexion.adaptive.utility.UUID
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test
import kotlin.test.assertEquals

@Adat
class EntityTestData(
    override val id: UUID<EntityTestData>,
    val someInt: Int,
    val someBoolean: Boolean
) : AdatEntity<EntityTestData>

@ExposedAdatTable
class UuidTestTable : AdatEntityTable<EntityTestData, UuidTestTable>() {

    val someInt = integer("some_int")
    val someBoolean = bool("some_boolean")

}

class AdatEntityTableTest {

    @Test
    fun basic() {
        InMemoryDatabase().create()
        UuidTestTable().apply {
            create()
            transaction {
                val td = EntityTestData(UUID(), 12, false).also { add(it) }
                val tdl = all()
                assertEquals(1, tdl.size)
                assertEquals(td, tdl.first())
            }
        }
    }
}