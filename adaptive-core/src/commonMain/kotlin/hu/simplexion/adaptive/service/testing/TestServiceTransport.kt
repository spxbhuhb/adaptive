/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service.testing

import hu.simplexion.adaptive.server.builtin.ServiceImpl
import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.service.defaultServiceImplFactory
import hu.simplexion.adaptive.service.factory.ServiceImplFactory
import hu.simplexion.adaptive.service.model.TransportEnvelope
import hu.simplexion.adaptive.service.transport.ServiceCallTransport
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatProvider
import hu.simplexion.adaptive.wireformat.protobuf.ProtoWireFormatProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class TestServiceTransport(
    val serviceImpl: ServiceImpl<*>? = null,
    val dump: Boolean = false,
    override val serviceImplFactory: ServiceImplFactory = defaultServiceImplFactory,
    override val wireFormatProvider: WireFormatProvider = ProtoWireFormatProvider(),
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