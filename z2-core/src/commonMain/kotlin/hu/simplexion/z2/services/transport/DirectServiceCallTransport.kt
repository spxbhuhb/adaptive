package hu.simplexion.z2.services.transport

import hu.simplexion.z2.services.BasicServiceContext
import hu.simplexion.z2.services.ServiceImpl
import hu.simplexion.z2.services.defaultServiceImplFactory
import hu.simplexion.z2.wireformat.Message
import hu.simplexion.z2.wireformat.WireFormatProvider.Companion.defaultWireFormatProvider

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

    override suspend fun call(serviceName: String, funName: String, payload: ByteArray): Message {
        val wireFormatConfig = defaultWireFormatProvider

        val message = wireFormatConfig.toMessage(payload)

        val responsePayload = implementation.newInstance(directServiceContext).dispatch(funName, message)

        return wireFormatConfig.toMessage(responsePayload)
    }

}