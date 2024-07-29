/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service

import hu.simplexion.adaptive.service.model.ResponseEnvelope
import hu.simplexion.adaptive.utility.UUID

open class ServiceContext(
    val uuid: UUID<ServiceContext> = UUID(),
    val data: MutableMap<Any, Any?> = mutableMapOf()
) {

    /**
     * Send a message to a listener on the other side of the connection this
     * context belongs to. The listener is identified by `envelope.callId`.
     */
    open suspend fun send(envelope: ResponseEnvelope) {
        throw UnsupportedOperationException()
    }

    /**
     * Register a cleanup function that runs when the connection is closed.
     */
    open fun connectionCleanup(cleanupFun: (id: UUID<ServiceContext>) -> Unit) {
        throw UnsupportedOperationException()
    }

    /**
     * Register a cleanup function that runs when the session is closed.
     */
    open fun sessionCleanup(cleanupFun: (id: UUID<ServiceContext>) -> Unit) {
        throw UnsupportedOperationException()
    }

    override fun toString(): String {
        return "ServiceContext(uuid=$uuid, data=$data)"
    }
}