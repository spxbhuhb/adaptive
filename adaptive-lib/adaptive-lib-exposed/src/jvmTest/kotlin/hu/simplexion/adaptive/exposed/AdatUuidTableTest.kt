/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.exposed

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.utility.UUID
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test
import kotlin.test.assertEquals

@Adat
class UuidTestData(
    val id: UUID<UuidTestData>,
    val someInt: Int,
    val someBoolean: Boolean
) : AdatClass<UuidTestData>

@ExposedAdatTable
class UuidTestTable : AdatUuidTable<UuidTestData, UuidTestTable>() {

    val someInt = integer("some_int")
    val someBoolean = bool("some_boolean")

}

class AdatUuidTableTest {

    @Test
    fun basic() {
        InMemoryDatabase().create()
        UuidTestTable().apply {
            create()
            transaction {
                val td = UuidTestData(UUID(), 12, false).also { add(it) }
                val tdl = all()
                assertEquals(1, tdl.size)
                assertEquals(td, tdl.first())
            }
        }
    }
}