/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.service.testing

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.defaultServiceImplFactory
import `fun`.adaptive.service.factory.ServiceImplFactory
import `fun`.adaptive.service.model.TransportEnvelope
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.api.Proto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class TestServiceTransport(
    val serviceImpl: ServiceImpl<*>? = null,
    val peerTransport: TestServiceTransport? = null,
    val dump: Boolean = false,
    override val serviceImplFactory: ServiceImplFactory = defaultServiceImplFactory,
    override val wireFormatProvider: WireFormatProvider = Proto
) : ServiceCallTransport(
    CoroutineScope(Dispatchers.Default)
) {

    override suspend fun send(envelope: TransportEnvelope) {
        receive(wireFormatProvider.encode(envelope, TransportEnvelope))
    }

    override fun context(): ServiceContext {
        return ServiceContext(transport = peerTransport ?: this)
    }

    override suspend fun dispatch(context: ServiceContext, serviceName: String, funName: String, decoder: WireFormatDecoder<*>): ByteArray =
        if (serviceImpl != null) {
            serviceImpl.newInstance(context).dispatch(funName, decoder)
        } else {
            checkNotNull(serviceImplFactory[serviceName, context]) { "missing service: $serviceName" }
                .dispatch(funName, decoder)
        }

    override suspend fun disconnect() {

    }

    override suspend fun stop() {

    }

}