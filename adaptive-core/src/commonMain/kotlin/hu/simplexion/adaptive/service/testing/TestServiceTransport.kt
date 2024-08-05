/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service.testing

import hu.simplexion.adaptive.server.builtin.ServiceImpl
import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.service.model.TransportEnvelope
import hu.simplexion.adaptive.service.transport.ServiceCallTransport
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatProvider
import hu.simplexion.adaptive.wireformat.protobuf.ProtoWireFormatProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class TestServiceTransport(
    val serviceImpl: ServiceImpl<*>,
    val dump: Boolean = false
) : ServiceCallTransport(
    CoroutineScope(Dispatchers.Default)
) {
    override val wireFormatProvider: WireFormatProvider
        get() = ProtoWireFormatProvider()

    override suspend fun send(envelope: TransportEnvelope) {
        receive(wireFormatProvider.encode(envelope, TransportEnvelope))
    }

    override fun context(): ServiceContext {
        return ServiceContext()
    }

    override suspend fun dispatch(context: ServiceContext, serviceName: String, funName: String, decoder: WireFormatDecoder<*>): ByteArray {
        return serviceImpl
            .newInstance(TestServiceContext(this))
            .also { it.serviceCallTransport = this }
            .dispatch(funName, decoder)
    }

    override suspend fun disconnect() {

    }

    override suspend fun stop() {

    }

}