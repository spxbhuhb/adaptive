/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.service.testing

import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.model.TransportEnvelope
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.api.Proto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

open class DirectServiceTransport(
    val dump: Boolean = false,
    override val wireFormatProvider: WireFormatProvider = Proto,
    name : String = "direct",
    val setupFun: suspend DirectServiceTransport.() -> Unit = {  }
) : ServiceCallTransport(
    CoroutineScope(Dispatchers.Default),
    name
) {

    val lock = getLock()

    /**
     * When `true` the transport silently drops received envelopes. This is intended for
     * testing connection breaks.
     */
    var drop = false
        get() = lock.use { field }
        set(value) = lock.use { field = value }

    var peerTransport: DirectServiceTransport? = null

    override suspend fun start(): ServiceCallTransport {
        setupFun.invoke(this)
        return super.start()
    }

    override suspend fun send(envelope: TransportEnvelope) {
        if (drop) return
        checkNotNull(peerTransport) { "peer transport not set (maybe disconnected)" }
            .receive(wireFormatProvider.encode(envelope, TransportEnvelope))
    }

    override fun context(): ServiceContext {
        return ServiceContext(transport = this)
    }

    override suspend fun dispatch(context: ServiceContext, serviceName: String, funName: String, decoder: WireFormatDecoder<*>): ByteArray =
        checkNotNull(serviceImplFactory[serviceName, context]) { "missing service: $serviceName" }
            .dispatch(funName, decoder)

    override suspend fun disconnect() {
        peerTransport = null
    }

    override suspend fun stop() {

    }

}