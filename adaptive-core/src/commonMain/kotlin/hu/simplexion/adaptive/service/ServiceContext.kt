/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service

import hu.simplexion.adaptive.service.model.ServiceSession
import hu.simplexion.adaptive.utility.CleanupHandler
import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.utility.getLock
import hu.simplexion.adaptive.utility.use
import kotlinx.atomicfu.atomic

open class ServiceContext<PT>(
    val uuid: UUID<ServiceContext<PT>> = UUID(),
    val sessionOrNull: ServiceSession<PT>? = null
) {

    val disconnect = atomic<Boolean>(false)

    private val lock = getLock()

    private val cleanupHandlers = mutableListOf<CleanupHandler<ServiceContext<PT>>>()

    val data = mutableMapOf<Any, Any?>()

    operator fun get(key: Any): Any? = lock.use { data[key] }

    operator fun set(key: Any, value: Any?) {
        lock.use { data[key] = value }
    }

    /**
     * Add a cleanup handler that runs after a service context is disconnected.
     */
    open fun addContextCleanup(handler: CleanupHandler<ServiceContext<PT>>) {
        lock.use { cleanupHandlers += handler }
    }

    /**
     * Remove a previously added context cleanup handler.
     */
    open fun removeContextCleanup(handler: CleanupHandler<ServiceContext<PT>>) {
        lock.use { cleanupHandlers -= handler }
    }

    /**
     * Add a cleanup handler that runs after the session is closed.
     */
    open fun addSessionCleanup(handler: CleanupHandler<ServiceSession<PT>>) {
        sessionOrNull?.addSessionCleanup(handler)
    }

    /**
     * Remove a previously added session cleanup handler.
     */
    open fun removeSessionCleanup(handler: CleanupHandler<ServiceSession<PT>>) {
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