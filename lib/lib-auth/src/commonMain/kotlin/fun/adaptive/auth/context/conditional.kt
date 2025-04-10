/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.context

import `fun`.adaptive.auth.model.AuthPrincipalId
import `fun`.adaptive.auth.model.AuthRole
import `fun`.adaptive.auth.model.AuthRoleId
import `fun`.adaptive.service.ServiceContext

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
fun ServiceContext.has(role: AuthRole): Boolean {
    val session = getSessionOrNull() ?: return false
    return session.roles.any { it == role.uuid }
}

/**
 * True when there is a session in the service context and the session has the [roleId].
 */
fun ServiceContext.has(roleId: AuthRoleId): Boolean {
    val session = getSessionOrNull() ?: return false
    return session.roles.any { it == roleId }
}

/**
 * True when there is a session in the service context and the session has **ALL** of
 * the [roles].
 */
fun ServiceContext.hasAll(vararg roles: AuthRole): Boolean {
    val session = getSessionOrNull() ?: return false
    for (role in roles) {
        if (! session.roles.any { it == role.uuid }) return false
    }
    return true
}

/**
 * True when there is a session in the service context and the session has **ALL** of
 * the [roleIds].
 */
fun ServiceContext.hasAll(vararg roleIds: AuthRoleId): Boolean {
    val session = getSessionOrNull() ?: return false
    for (role in roleIds) {
        if (! session.roles.any { it == role }) return false
    }
    return true
}

/**
 * True when there is a session in the service context and the session has
 * **AT LEAST ONE** of the [roles].
 */
fun ServiceContext.hasOneOf(vararg roles: AuthRole): Boolean {
    val session = getSessionOrNull() ?: return false
    for (role in roles) {
        if (session.roles.any { it == role.uuid }) return true
    }
    return false
}

/**
 * True when there is a session in the service context and the session has
 * **AT LEAST ONE** of the [roleIds].
 */
fun ServiceContext.hasOneOf(vararg roleIds: AuthRoleId): Boolean {
    val session = getSessionOrNull() ?: return false
    for (role in roleIds) {
        if (session.roles.any { it == role }) return true
    }
    return false
}

/**
 * True when there is a session in the service context and the principal
 * of the session is [principalId].
 */
fun ServiceContext.ofPrincipal(principalId: AuthPrincipalId): Boolean {
    return principalId == sessionOrNull?.principalOrNull
}