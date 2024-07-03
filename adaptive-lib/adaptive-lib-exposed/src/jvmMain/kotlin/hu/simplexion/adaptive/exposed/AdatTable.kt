/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.exposed

import hu.simplexion.adaptive.adat.AdatClass
import org.jetbrains.exposed.sql.Table

/**
 * A [Table] with Adat field mapping. The compiler plugin generates
 * `fromRow` and `toRow` functions to map between Exposed and the Adat
 * class this table stores.
 *
 * Check the reference documentation for exact details of the mapping.
 */
abstract class AdatTable<A : AdatClass<A>, S : AdatTable<A, S>>(
    tableName: String = ""
) : Table(tableName), ExposedStoreImpl<A, S>