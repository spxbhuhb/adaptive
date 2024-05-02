/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service.transport

import hu.simplexion.adaptive.service.BasicServiceContext
import hu.simplexion.adaptive.server.builtin.ServiceImpl
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.defaultWireFormatProvider

/**
 * Service call transport that uses the given implementation directly, ignoring [defaultServiceImplFactory].
 *
 * This class is intended for testing. For direct calls using the implementation directly is usually better
 * as it avoids the encoding and decoding overhead.
 */
class DirectServiceCallTransport(
    val implementation: ServiceImpl<*>
) : ServiceCallTransport {

    companion object {
        val directServiceContext = BasicServiceContext()
    }

    override suspend fun call(serviceName: String, funName: String, payload: ByteArray): ByteArray =
        implementation.newInstance(directServiceContext).dispatch(funName, defaultWireFormatProvider.decoder(payload))

}