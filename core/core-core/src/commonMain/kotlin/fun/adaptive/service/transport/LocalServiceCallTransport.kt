/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.service.transport

import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.model.TransportEnvelope
import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.api.Proto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class LocalServiceCallTransport(
    val dump: Boolean = false,
    override val wireFormatProvider: WireFormatProvider = Proto
) : ServiceCallTransport(
    CoroutineScope(Dispatchers.Default)
) {

    override suspend fun send(envelope: TransportEnvelope) {
        receive(wireFormatProvider.encode(envelope, TransportEnvelope))
    }

    override fun context(): ServiceContext {
        return ServiceContext(transport = this)
    }

    override suspend fun dispatch(context: ServiceContext, serviceName: String, funName: String, decoder: WireFormatDecoder<*>): ByteArray =
        checkNotNull(serviceImplFactory[serviceName, context]) { "missing service: $serviceName" }
            .dispatch(funName, decoder)

    override suspend fun disconnect() {

    }

    override suspend fun stop() {

    }

}