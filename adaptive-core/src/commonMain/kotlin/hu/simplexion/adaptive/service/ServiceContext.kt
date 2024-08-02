/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service

import hu.simplexion.adaptive.foundation.unsupported
import hu.simplexion.adaptive.service.transport.ServiceCallTransport
import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.wireformat.WireFormatProvider

open class ServiceContext(
    val uuid: UUID<ServiceContext> = UUID(),
    val data: MutableMap<Any, Any?> = mutableMapOf(),
    override val wireFormatProvider: WireFormatProvider
) : ServiceCallTransport() {

    /**
     * Register a cleanup function that runs when the connection is closed.
     */
    open fun connectionCleanup(cleanupFun: (id: UUID<ServiceContext>) -> Unit) {
        unsupported()
    }

    /**
     * Register a cleanup function that runs when the session is closed.
     */
    open fun sessionCleanup(cleanupFun: (id: UUID<ServiceContext>) -> Unit) {
        unsupported()
    }

    override fun toString(): String {
        return "ServiceContext(uuid=$uuid, data=$data)"
    }
}