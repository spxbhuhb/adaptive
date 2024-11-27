/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.cookbook.auth

import `fun`.adaptive.cookbook.auth.model.Account
import `fun`.adaptive.exposed.AdatEntityTable
import `fun`.adaptive.exposed.ExposedAdatTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

@ExposedAdatTable
object AccountTable : AdatEntityTable<Account, AccountTable>("account") {

    val name = varchar("name", 255)
    val email = varchar("email", 255)
    val phone = varchar("phone", 255)

}