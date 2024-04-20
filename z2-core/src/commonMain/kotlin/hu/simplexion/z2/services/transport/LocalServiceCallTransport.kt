package hu.simplexion.z2.services.transport

import hu.simplexion.z2.services.BasicServiceContext
import hu.simplexion.z2.services.defaultServiceImplFactory
import hu.simplexion.z2.wireformat.Message
import hu.simplexion.z2.wireformat.WireFormatProvider.Companion.defaultWireFormatProvider

/**
 * Get the service from the implementation factory, execute the function with it and
 * returns with the result.
 */
class LocalServiceCallTransport : ServiceCallTransport {

    companion object {
        // FIXME local service context UUID
        val localServiceContext = BasicServiceContext()
    }

    override suspend fun call(serviceName: String, funName: String, payload: ByteArray): Message {

        val service = requireNotNull(defaultServiceImplFactory[serviceName, localServiceContext])

        val wireFormatConfig = defaultWireFormatProvider
        val message = wireFormatConfig.toMessage(payload)

        val responsePayload = service.dispatch(funName, message)
        val responseMessage = wireFormatConfig.toMessage(responsePayload)

        return responseMessage
    }

}