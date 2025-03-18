package `fun`.adaptive.auth.util

import `fun`.adaptive.auth.model.Role
import `fun`.adaptive.lib.auth.store.RoleTable
import `fun`.adaptive.utility.UUID.Companion.uuid7

actual fun getRoleFor(operationName : String, signature: String): Role {
    val existing = RoleTable.getByNameOrNull("query", signature)

    if (existing != null) {
        return existing
    }

    val new = Role(uuid7(), name = "query", context = signature)
    RoleTable += new
    return new
}