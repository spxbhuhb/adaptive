/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.auth.context

import hu.simplexion.adaptive.auth.model.Principal
import hu.simplexion.adaptive.auth.model.Role
import hu.simplexion.adaptive.server.builtin.ServiceImpl
import hu.simplexion.adaptive.utility.UUID

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
fun ServiceImpl<*>.ensureHasOrInternal(roleUuid: UUID<Role>) {
    if (serviceContext.isInternal()) return
    ensureHas(roleUuid)
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
fun ServiceImpl<*>.ensureHas(roleUuid: UUID<Role>) {
    if (! serviceContext.has(roleUuid)) throw AccessDenied()
}

/**
 * Ensures that the context has **ALL** of the specified roles.
 *
 * @throws   AccessDenied  At least one of the roles is not in the context.
 */
fun ServiceImpl<*>.ensureAll(vararg roles: Role) {
    if (! serviceContext.hasAll(*roles)) throw AccessDenied()
}

/**
 * Ensures that the context has **ALL** of the specified roles.
 *
 * @throws   AccessDenied  At least one of the roles is not in the context.
 */
fun ServiceImpl<*>.ensureAll(vararg roles: UUID<Role>) {
    if (! serviceContext.hasAll(*roles)) throw AccessDenied()
}

/**
 * Ensures that the context has **AT LEAST ONE** of the specified roles.
 *
 * @throws   AccessDenied  None of the roles are in the context.
 */
fun ServiceImpl<*>.ensureOneOf(vararg roles: Role) {
    if (! serviceContext.hasOneOf(*roles)) throw AccessDenied()
}

/**
 * Ensures that the context has **AT LEAST ONE** of the specified roles.
 *
 * @throws   AccessDenied  None of the roles are in the context.
 */
fun ServiceImpl<*>.ensureOneOf(vararg roleUuids: UUID<Role>) {
    if (! serviceContext.hasOneOf(*roleUuids)) throw AccessDenied()
}

/**
 * Ensures that the [result] is true.
 *
 * @throws   AccessDenied  [result] is false
 */
fun ensurePrincipal(result: Boolean) {
    if (! result) throw AccessDenied()
}

/**
 * Allows the call if [block] returns with true.
 *
 * @throws  AccessDenied  If [block] returns with false.
 */
fun ensuredBy(block: () -> Boolean) {
    if (! block()) throw AccessDenied()
}

/**
 * Ensure that the service context runs in the name of the principal specified.
 */
fun ServiceImpl<*>.ensurePrincipal(principal: UUID<Principal>) {
    ensurePrincipal(serviceContext.isPrincipal(principal))
}

/**
 * Marks the following code as secure by some logic.
 */
fun ensuredByLogic(@Suppress("UNUSED_PARAMETER") explanation: String) {
    // nothing to do here, this is just a marker
}

/**
 * Marks the following as publicly accessible.
 */
fun publicAccess() {
    // nothing to do here, this is just a marker
}

/**
 * Ensure that this code fails with NotImplementedError.
 *
 * @throws  AccessDenied
 */
fun ensureFailNotImplemented(message: () -> String) {
    throw NotImplementedError(message())
}