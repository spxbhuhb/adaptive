/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.service.auth

import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.utility.UUID

typealias PrincipalId = UUID<*>
typealias RoleId = UUID<*>

/**
 * True when there is no service context for the call
 *
 * This happens only when the context-less version of the function is called internally
 * on the server side. The context-less version cannot be called through dispatch.
 */
fun ServiceContext.isInternal(): Boolean {
    return this.uuid.isNil
}

/**
 * True when there is a session in the service context and there is a principal in
 * the session.
 */
fun ServiceContext.isLoggedIn(): Boolean {
    val session = sessionOrNull ?: return false
    return session.principalOrNull != null
}

/**
 * True when there is a session in the service context and the session has the [roleId].
 */
fun ServiceContext.has(roleId: RoleId): Boolean {
    val session = sessionOrNull ?: return false
    return session.roles.any { it == roleId }
}

/**
 * True when there is a session in the service context and the session has **ALL** of
 * the [roleIds].
 */
fun ServiceContext.hasAll(vararg roleIds: RoleId): Boolean {
    val session = sessionOrNull ?: return false
    for (role in roleIds) {
        if (! session.roles.any { it == role }) return false
    }
    return true
}

/**
 * True when there is a session in the service context and the session has
 * **AT LEAST ONE** of the [roleIds].
 */
fun ServiceContext.hasOneOf(vararg roleIds: RoleId): Boolean {
    val session = sessionOrNull ?: return false
    for (role in roleIds) {
        if (session.roles.any { it == role }) return true
    }
    return false
}

/**
 * True when there is a session in the service context and the principal
 * of the session is [principalId].
 */
fun ServiceContext.ofPrincipal(principalId: PrincipalId): Boolean {
    return principalId == sessionOrNull?.principalOrNull
}