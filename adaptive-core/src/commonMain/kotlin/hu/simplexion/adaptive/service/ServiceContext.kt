/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service

import hu.simplexion.adaptive.service.model.ServiceSession
import hu.simplexion.adaptive.service.transport.ServiceCallTransport
import hu.simplexion.adaptive.utility.CleanupHandler
import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.utility.getLock
import hu.simplexion.adaptive.utility.use

open class ServiceContext(
    val uuid: UUID<ServiceContext> = UUID(),
    val sessionOrNull: ServiceSession? = null,
    val transport: ServiceCallTransport? = null
) {
    val session: ServiceSession
        get() = checkNotNull(sessionOrNull) { "missing or invalid session" }

    private val lock = getLock()

    var disconnect = false
        get() = lock.use { field }
        set(v) {
            lock.use { field = v }
        }

    private val cleanupHandlers = mutableListOf<CleanupHandler<ServiceContext>>()

    private val data = mutableMapOf<Any, Any?>()

    operator fun get(key: Any): Any? = lock.use { data[key] }

    operator fun set(key: Any, value: Any?) {
        lock.use { data[key] = value }
    }

    /**
     * Add a cleanup handler that runs after a service context is disconnected.
     */
    open fun addContextCleanup(handler: CleanupHandler<ServiceContext>) {
        lock.use { cleanupHandlers += handler }
    }

    /**
     * Remove a previously added context cleanup handler.
     */
    open fun removeContextCleanup(handler: CleanupHandler<ServiceContext>) {
        lock.use { cleanupHandlers -= handler }
    }

    /**
     * Add a cleanup handler that runs after the session is closed.
     */
    open fun addSessionCleanup(handler: CleanupHandler<ServiceSession>) {
        sessionOrNull?.addSessionCleanup(handler)
    }

    /**
     * Remove a previously added session cleanup handler.
     */
    open fun removeSessionCleanup(handler: CleanupHandler<ServiceSession>) {
        sessionOrNull?.removeSessionCleanup(handler)
    }

    fun cleanup() {
        lock.use {
            cleanupHandlers.forEach { it.cleanupFun.invoke(this) }
            cleanupHandlers.clear()
        }
    }

    override fun toString(): String {
        return "ServiceContext(uuid=$uuid, data=$data)"
    }
}