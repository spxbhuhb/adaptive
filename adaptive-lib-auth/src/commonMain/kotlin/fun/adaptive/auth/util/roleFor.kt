package `fun`.adaptive.auth.util

import `fun`.adaptive.auth.model.Role

fun getQueryRoleFor(signature: String): Role {
    return getRoleFor("query", signature)
}

fun getAddRoleFor(signature: String): Role {
    return getRoleFor("add", signature)
}

fun getUpdateRoleFor(signature: String): Role {
    return getRoleFor("update", signature)
}

fun getDeleteRoleFor(signature: String): Role {
    return getRoleFor("delete", signature)
}

expect fun getRoleFor(operationName : String, signature: String): Role