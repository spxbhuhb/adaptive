/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service.testing

import hu.simplexion.adaptive.server.builtin.ServiceImpl
import hu.simplexion.adaptive.service.ServiceResponseEndpoint
import hu.simplexion.adaptive.service.transport.ServiceCallTransport
import hu.simplexion.adaptive.service.transport.ServiceResponseListener
import hu.simplexion.adaptive.utility.getLock
import hu.simplexion.adaptive.utility.use
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.defaultWireFormatProvider

class TestServiceTransport(
    val serviceImpl: ServiceImpl<*>,
    val dump: Boolean = false
) : ServiceCallTransport() {

    val listenerLock = getLock()
    private val listeners = mutableMapOf<ServiceResponseEndpoint, ServiceResponseListener>()

    override suspend fun call(serviceName: String, funName: String, payload: ByteArray): ByteArray {
        if (dump) {
            println("==== REQUEST ====")
            println(serviceName)
            println(funName)
            println(defaultWireFormatProvider.dump(payload))
        }

        val responsePayload = serviceImpl
            .newInstance(TestServiceContext(this))
            .also { it.serviceCallTransport = this }
            .dispatch(funName, wireFormatDecoder(payload))

        if (dump) {
            println("==== RESPONSE ====")
            println(defaultWireFormatProvider.dump(responsePayload))
        }

        return responsePayload
    }

    operator fun get(index: ServiceResponseEndpoint): ServiceResponseListener? =
        listenerLock.use { listeners[index] }

    override fun connect(endpoint: ServiceResponseEndpoint, listener: ServiceResponseListener) {
        listenerLock.use {
            listeners[endpoint] = listener
        }
    }

    override fun disconnect(endpoint: ServiceResponseEndpoint) {
        listenerLock.use {
            listeners.remove(endpoint)
        }
    }
}