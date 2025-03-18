package `fun`.adaptive.auth.util

import `fun`.adaptive.auth.model.Role
import `fun`.adaptive.utility.UUID.Companion.uuid4

actual fun getRoleFor(operationName: String, signature: String): Role {
    return Role(uuid4(), operationName, signature)
}