package `fun`.adaptive.auth.util

import `fun`.adaptive.auth.model.AuthRole

fun getQueryRoleFor(signature: String): AuthRole {
    return getRoleFor("query", signature)
}

fun getAddRoleFor(signature: String): AuthRole {
    return getRoleFor("add", signature)
}

fun getUpdateRoleFor(signature: String): AuthRole {
    return getRoleFor("update", signature)
}

fun getDeleteRoleFor(signature: String): AuthRole {
    return getRoleFor("delete", signature)
}

expect fun getRoleFor(operationName: String, signature: String): AuthRole