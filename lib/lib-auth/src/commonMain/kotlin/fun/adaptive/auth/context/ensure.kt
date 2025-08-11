/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.context

import `fun`.adaptive.auth.model.AccessDenied
import `fun`.adaptive.auth.model.AuthPrincipalId
import `fun`.adaptive.auth.model.AuthRole
import `fun`.adaptive.auth.model.AuthRoleId
import `fun`.adaptive.backend.builtin.ServiceImpl

/**
 * Ensures there is no service context for the call.
 *
 * This happens only when the context-less version of the function is called internally
 * on the server side. The context-less version cannot be called through dispatch.
 *
 * @throws   AccessDenied  There is a service context.
 */
fun ServiceImpl<*>.ensureInternal() {
    if (! serviceContext.isInternal()) throw AccessDenied()
}

/**
 * Ensures that the call is internal or the principal has the given role.
 *
 * @throws   AccessDenied  The call is not internal and the principal does not have the given role.
 */
fun ServiceImpl<*>.ensureHasOrInternal(roleId : AuthRoleId) {
    if (serviceContext.isInternal()) return
    ensureHas(roleId)
}

/**
 * Ensures that there is a principal in the context (Session.principal != null).
 *
 * @throws   AccessDenied  There is no principal in the context.
 */
fun ServiceImpl<*>.ensureLoggedIn() {
    if (! serviceContext.isLoggedIn()) throw AccessDenied()
}

/**
 * Ensures that the context has the specified role.
 *
 * @throws   AccessDenied  At least one of the roles is not in the context.
 */
fun ServiceImpl<*>.ensureHas(roleId : AuthRoleId) {
    if (! serviceContext.has(roleId)) throw AccessDenied()
}

/**
 * Ensures that the context has **ALL** of the specified roles.
 *
 * @throws   AccessDenied  At least one of the roles is not in the context.
 */
fun ServiceImpl<*>.ensureAll(vararg roles : AuthRole) {
    if (! serviceContext.hasAll(*roles)) throw AccessDenied()
}

/**
 * Ensures that the context has **ALL** of the specified roles.
 *
 * @throws   AccessDenied  At least one of the roles is not in the context.
 */
fun ServiceImpl<*>.ensureAll(vararg roleIds : AuthRoleId) {
    if (! serviceContext.hasAll(*roleIds)) throw AccessDenied()
}

/**
 * Ensures that the context has **AT LEAST ONE** of the specified roles.
 *
 * @throws   AccessDenied  None of the roles are in the context.
 */
fun ServiceImpl<*>.ensureOneOf(vararg roles : AuthRole) {
    if (! serviceContext.hasOneOf(*roles)) throw AccessDenied()
}

/**
 * Ensures that the context has **AT LEAST ONE** of the specified roles.
 *
 * @throws   AccessDenied  None of the roles are in the context.
 */
fun ServiceImpl<*>.ensureOneOf(vararg roleIds : AuthRoleId) {
    if (! serviceContext.hasOneOf(*roleIds)) throw AccessDenied()
}

/**
 * Ensures that the [result] is true.
 *
 * @throws   AccessDenied  [result] is false
 */
fun ensureTrue(result : Boolean) {
    if (! result) throw AccessDenied()
}

/**
 * Allows the call if [block] returns with true.
 *
 * @throws  AccessDenied  If [block] returns with false.
 */
fun ensuredBy(block : () -> Boolean) {
    if (! block()) throw AccessDenied()
}

/**
 * Ensure that the service context runs in the name of the principal specified.
 */
fun ServiceImpl<*>.ensurePrincipal(principalId : AuthPrincipalId) {
    ensureTrue(serviceContext.ofPrincipal(principalId))
}

/**
 * Ensure that the service context runs in the name of the principal specified **OR**
 * it has **AT LEASE ONE** of the specified roles.
 */
fun ServiceImpl<*>.ensurePrincipalOrHas(principalId : AuthPrincipalId?, roleId : AuthRoleId) {
    if (principalId != null && serviceContext.ofPrincipal(principalId)) return
    ensureHas(roleId)
}

/**
 * Ensure that the service context runs in the name of the principal specified **OR**
 * it has **AT LEASE ONE** of the specified roles.
 */
fun ServiceImpl<*>.ensurePrincipalOrOneOf(principalId : AuthPrincipalId, roleIds : Array<AuthRoleId>) {
    if (serviceContext.ofPrincipal(principalId)) return
    ensureOneOf(*roleIds)
}

/**
 * Marks the following code as secure by some logic.
 */
fun ensuredByLogic(@Suppress("UNUSED_PARAMETER") explanation : String) {
    // nothing to do here, this is just a marker
}

/**
 * Marks the following as publicly accessible.
 */
fun publicAccess() {
    // nothing to do here, this is just a marker
}

/**
 * Ensure that this code fails with `NotImplementedError`.
 *
 * @throws  NotImplementedError
 */
fun ensureFailNotImplemented(message : () -> String) {
    throw NotImplementedError(message())
}