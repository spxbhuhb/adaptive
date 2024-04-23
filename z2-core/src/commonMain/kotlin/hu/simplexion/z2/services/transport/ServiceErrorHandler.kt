package hu.simplexion.z2.services.transport

import hu.simplexion.z2.services.model.ResponseEnvelope

/**
 * Handle service error responses.
 */
open class ServiceErrorHandler {

    open fun connectionError(ex: Exception) {

    }

    open fun callError(serviceName: String, funName: String, responseEnvelope: ResponseEnvelope) {

    }

}