/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.service

import `fun`.adaptive.service.model.ServiceSession
import `fun`.adaptive.service.transport.FileTransport
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.CleanupHandler
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use

/**
 *  A context that belongs to one connection. It is not the same as a session,
 *  as one session may have more than one connection and thus more than one
 *  context.
 *
 * @property  uuid       The unique id of this service context.
 * @property  transport  The service transport to send calls to the connected peer.
 */
open class ServiceContext(
    val transport: ServiceCallTransport,
    val uuid: UUID<ServiceContext> = UUID(),
    val sessionOrNull: ServiceSession? = null,
    val fileTransport : FileTransport? = null
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