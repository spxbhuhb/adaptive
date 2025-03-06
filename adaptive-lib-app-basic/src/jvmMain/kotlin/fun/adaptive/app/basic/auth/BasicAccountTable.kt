package `fun`.adaptive.app.basic.auth

import `fun`.adaptive.app.basic.auth.model.BasicAccount
import `fun`.adaptive.exposed.AdatEntityTable
import `fun`.adaptive.exposed.ExposedAdatTable

@ExposedAdatTable
object BasicAccountTable : AdatEntityTable<BasicAccount, BasicAccountTable>("account") {

    val name = varchar("name", 255)
    val email = varchar("email", 255)
    val phone = varchar("phone", 255)

}