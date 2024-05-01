/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.exposed

import hu.simplexion.adaptive.server.component.StoreImpl
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

interface ExposedStoreImpl<T : ExposedStoreImpl<T>> : StoreImpl<T, Any> {

    override fun create() {
        if (this is Table) {
            transaction {
                SchemaUtils.createMissingTablesAndColumns(this@ExposedStoreImpl)
            }
        }
    }

    fun isEmpty() : Boolean =
        count() == 0L

    fun isNotEmpty() : Boolean =
        count() != 0L

    fun count() =
        (this as Table).selectAll().count()

}