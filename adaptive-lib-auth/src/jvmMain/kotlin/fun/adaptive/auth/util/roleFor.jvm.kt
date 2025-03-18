package `fun`.adaptive.auth.util

import `fun`.adaptive.auth.model.Role
import `fun`.adaptive.lib.auth.store.RoleTable
import `fun`.adaptive.utility.UUID.Companion.uuid7
import org.jetbrains.exposed.sql.transactions.transaction

actual fun getRoleFor(operationName : String, signature: String): Role =
    transaction {
        val existing = RoleTable.getByNameOrNull("query", signature)

        if (existing != null) {
            return@transaction existing
        }

        val new = Role(uuid7(), name = "query", context = signature)
        RoleTable += new
        return@transaction new
    }