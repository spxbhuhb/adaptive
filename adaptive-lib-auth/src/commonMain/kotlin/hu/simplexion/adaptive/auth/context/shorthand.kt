/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.auth.context

import hu.simplexion.adaptive.auth.model.Session
import hu.simplexion.adaptive.auth.model.Session.Companion.SESSION_TOKEN
import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.service.get
import hu.simplexion.adaptive.utility.UUID
import kotlin.let

val ServiceContext.isLoggedIn: Boolean
    get() = getSessionOrNull()?.principal.let { it != null && it != UUID.NIL }

/**
 * Get the current session of the service context.
 *
 * @throws  IllegalStateException  If there is no session associated with the context.
 */
fun ServiceContext.getSession(): Session =
    checkNotNull(getSessionOrNull()) { "missing or invalid session" }

/**
 * Get the current session of the service context or null if there is no session
 * associated with the context.
 */
fun ServiceContext.getSessionOrNull() =
    this[SESSION_TOKEN]

/**
 * Get the principal of the session if there is one, throw exception if there isn't.
 *
 * @throws  IllegalStateException  when there is no session or there is no principal
 */
fun ServiceContext.getPrincipal() =
    checkNotNull(getSessionOrNull()?.principal) { "there is no session in the service context" }

/**
 * Get the principal of the session if there is one or null if there isn't.
 */
fun ServiceContext.getPrincipalOrNull() =
    getSessionOrNull()?.principal