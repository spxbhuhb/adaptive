/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service.transport

import hu.simplexion.adaptive.service.ServiceResponseEndpoint
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.defaultWireFormatProvider

abstract class ServiceCallTransport {

    abstract suspend fun call(serviceName: String, funName: String, payload: ByteArray): ByteArray

    abstract fun connect(endpoint: ServiceResponseEndpoint, listener: ServiceResponseListener)

    abstract fun disconnect(endpoint: ServiceResponseEndpoint)

    val wireFormatEncoder: WireFormatEncoder
        get() = defaultWireFormatProvider.encoder()

    fun wireFormatDecoder(payload: ByteArray): WireFormatDecoder<*> =
        defaultWireFormatProvider.decoder(payload)

}