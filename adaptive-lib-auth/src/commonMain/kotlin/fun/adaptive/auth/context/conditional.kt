/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.context

import `fun`.adaptive.auth.model.Principal
import `fun`.adaptive.auth.model.Role
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.utility.UUID

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
 * True when there is a session in the service context and the session has the [role].
 */
fun ServiceContext.has(role: Role): Boolean {
    val session = getSessionOrNull() ?: return false
    return session.roles.any { it.id == role.id }
}

/**
 * True when there is a session in the service context and the session has the [role].
 */
fun ServiceContext.has(role: UUID<Role>): Boolean {
    val session = getSessionOrNull() ?: return false
    return session.roles.any { it.id == role }
}

/**
 * True when there is a session in the service context and the session has **ALL** of
 * the [roles].
 */
fun ServiceContext.hasAll(vararg roles: Role): Boolean {
    val session = getSessionOrNull() ?: return false
    for (role in roles) {
        if (! session.roles.any { it.id == role.id }) return false
    }
    return true
}

/**
 * True when there is a session in the service context and the session has **ALL** of
 * the [roles].
 */
fun ServiceContext.hasAll(vararg roles: UUID<Role>): Boolean {
    val session = getSessionOrNull() ?: return false
    for (role in roles) {
        if (! session.roles.any { it.id == role }) return false
    }
    return true
}

/**
 * True when there is a session in the service context and the session has
 * **AT LEAST ONE** of the [roles].
 */
fun ServiceContext.hasOneOf(vararg roles: Role): Boolean {
    val session = getSessionOrNull() ?: return false
    for (role in roles) {
        if (session.roles.any { it.id == role.id }) return true
    }
    return false
}

/**
 * True when there is a session in the service context and the session has
 * **AT LEAST ONE** of the [roles].
 */
fun ServiceContext.hasOneOf(vararg roles: UUID<Role>): Boolean {
    val session = getSessionOrNull() ?: return false
    for (role in roles) {
        if (session.roles.any { it.id == role }) return true
    }
    return false
}

/**
 * True when there is a session in the service context and the principal
 * of the session is [principal].
 */
fun ServiceContext.ofPrincipal(principal: UUID<Principal>): Boolean {
    return principal == sessionOrNull?.principalOrNull
}