package `fun`.adaptive.auth.util

import `fun`.adaptive.auth.model.AuthRole

actual fun getRoleFor(operationName: String, signature: String): AuthRole =
    TODO()
//    transaction {
//        val existing = RoleTable.getByNameOrNull("query", signature)
//
//        if (existing != null) {
//            return@transaction existing
//        }
//
//        val new = Role(uuid7(), name = "query", context = signature)
//        RoleTable += new
//        return@transaction new
//    }