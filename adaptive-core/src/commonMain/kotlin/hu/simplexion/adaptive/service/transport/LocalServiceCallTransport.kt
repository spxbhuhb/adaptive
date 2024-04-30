/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service.transport

import hu.simplexion.adaptive.service.BasicServiceContext
import hu.simplexion.adaptive.service.defaultServiceImplFactory
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.defaultWireFormatProvider

/**
 * Get the service from the implementation factory, execute the function with it and
 * returns with the result.
 */
class LocalServiceCallTransport : ServiceCallTransport {

    companion object {
        // FIXME local service context UUID
        val localServiceContext = BasicServiceContext()
    }

    override suspend fun call(serviceName: String, funName: String, payload: ByteArray): ByteArray {

        val service = requireNotNull(defaultServiceImplFactory[serviceName, localServiceContext]) { "missing service $serviceName" }
        val message = defaultWireFormatProvider.decoder(payload)

        return service.dispatch(funName, message)
    }

}